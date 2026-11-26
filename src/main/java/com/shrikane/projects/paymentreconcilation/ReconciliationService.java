package com.shrikane.projects.paymentreconcilation;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReconciliationService {

    /**
     * Everything a payment reads or writes, kept together so a batch can stage a copy of
     * BOTH and commit or discard them as one unit.
     *
     * @param ledger invoiceId -> Invoice
     * @param cache idempotency key -> what we answered the first time
     */
    private record State(Map<String, Invoice> ledger, Map<String, IdempotencyEntry> cache) {
        static State empty() {
            return new State(new LinkedHashMap<>(), new HashMap<>());
        }

        /**
         * Shallow copies are enough: Invoice and IdempotencyEntry are immutable, so the
         * copy can never change an object the live state still points to.
         */
        State copy() {
            return new State(new LinkedHashMap<>(ledger), new HashMap<>(cache));
        }
    }

    /** Live state. Only read or replaced while holding {@link #lock}. */
    private State state = State.empty();

    /**
     * Guards ALL shared state. Anything that writes (register, pay, batch) takes the
     * write lock for its whole check-then-act; read-only listing takes the read lock.
     * Pure work (parsing) happens before the lock is taken.
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ReconciliationService() {}

    public Response registerInvoices(Object invoices) {
        if (!(invoices instanceof List<?> rows) || rows.isEmpty()) {
            throw Parsing.invalidInput();
        }

        // Pass 1 (no lock needed: only parses strings into a local map nobody else sees).
        // Validate every row in input order. A duplicate inside the batch is
        // reported at the row where it appears, so "first encountered error wins".
        Map<String, Invoice> batch = new LinkedHashMap<>();
        for (Object row : rows) {
            Invoice invoice = Parsing.invoice(row);
            if (batch.containsKey(invoice.invoiceId())) {
                throw new ServiceError(409, "duplicate_invoice");
            }
            batch.put(invoice.invoiceId(), invoice);
        }

        // Pass 2 + store: the check and the write must be one unit, so both happen
        // under the write lock. Nobody can add the same ID between the check and putAll.
        lock.writeLock().lock();
        try {
            for (String invoiceId : batch.keySet()) {
                if (state.ledger().containsKey(invoiceId)) {
                    throw new ServiceError(409, "duplicate_invoice");
                }
            }
            // Nothing above touched the ledger, so any failure left it unchanged (atomic).
            state.ledger().putAll(batch);
        } finally {
            lock.writeLock().unlock(); // finally: released even when we throw the 409
        }
        return new Response(201, Values.row("registered_count", batch.size()));
    }

    public Response reconcilePayment(Object payment) {
        return reconcilePayment(payment, 0, null); // documented defaults
    }

    public Response reconcilePayment(Object payment, Object forgiveness, Object key) {
        // 1. Validate first, outside the lock. An invalid request never reaches the
        //    cache, so its key stays unused and a corrected retry can still use it.
        PaymentRequest request = Parsing.payment(payment, forgiveness, key);

        // 2. Key lookup, matching, settling and storing are ONE unit under the write lock.
        lock.writeLock().lock();
        try {
            return process(state, request);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * One payment against the given state: the live state for single calls, a staged
     * copy for batches. Caller must hold the write lock.
     */
    private static Response process(State target, PaymentRequest request) {
        // 3. Early exit for a retry: same key + same request -> replay, never re-match.
        if (request.key() != null) {
            IdempotencyEntry cached = target.cache().get(request.key());
            if (cached != null) {
                if (!cached.sameRequestAs(request)) {
                    throw new ServiceError(409, "idempotency_conflict");
                }
                // Fresh copy per replay: callers can't corrupt the cache or each other.
                return new Response(200, Values.copy(cached.body()));
            }
        }

        // 4. First attempt for this key (or no key at all): run the tiers.
        Optional<MatchTier.Match> match = MatchTier.firstMatch(request, target.ledger());

        Response response;
        if (match.isPresent()) {
            Invoice invoice = match.get().invoice();
            // Invoices are immutable: replace the entry with its PAID version. It is no
            // longer UNPAID, so no later payment can match it.
            target.ledger().put(invoice.invoiceId(), invoice.settledBy(request.paymentId()));
            response = new Response(201, matchedBody(request, invoice, match.get().tier()));
        } else {
            response = new Response(200, unmatchedBody(request));
        }

        // 5. Remember the answer (matched AND unmatched) so a retry replays it.
        //    IdempotencyEntry.of deep-copies, so the caller's map and ours are separate.
        if (request.key() != null) {
            target.cache().put(request.key(), IdempotencyEntry.of(request, castBody(response)));
        }
        return response;
    }

    private static Map<String, Object> matchedBody(
            PaymentRequest request, Invoice invoice, MatchTier tier) {
        return Values.row(
                "status", "MATCHED",
                "payment_id", request.paymentId(),
                "settled_invoice_id", invoice.invoiceId(),
                "tier_used", tier.label,
                "message", "Payment " + request.paymentId() + " paid " + request.amount()
                        + " for invoice " + invoice.invoiceId() + " due on " + invoice.dueDate());
    }

    private static Map<String, Object> unmatchedBody(PaymentRequest request) {
        // Values.row builds a LinkedHashMap, which allows the two null values.
        return Values.row(
                "status", "UNMATCHED",
                "payment_id", request.paymentId(),
                "settled_invoice_id", null,
                "tier_used", null,
                "message", "Payment " + request.paymentId() + " could not be matched to any invoice");
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castBody(Response response) {
        return (Map<String, Object>) response.body();
    }

    /** Due date ascending, then invoice ID in Unicode code-point order. */
    private static final Comparator<Invoice> LISTING_ORDER =
            Comparator.comparing(Invoice::dueDate)
                    .thenComparing(Invoice::invoiceId, UnicodeStrings::compare);

    public Response listInvoices() {
        // Under the read lock, only take a snapshot. Invoices are immutable, so copying
        // the references is enough: nobody can change these objects after we release.
        List<Invoice> snapshot;
        lock.readLock().lock();
        try {
            snapshot = new ArrayList<>(state.ledger().values());
        } finally {
            lock.readLock().unlock();
        }

        // Sorting and building output maps is pure work on our private copy: no lock.
        snapshot.sort(LISTING_ORDER);
        List<Map<String, Object>> rows = new ArrayList<>(snapshot.size());
        for (Invoice invoice : snapshot) {
            rows.add(invoice.toRow()); // fresh map each time -> detached from our state
        }
        return new Response(200, rows);
    }

    /**
     * Result of parsing one batch row ahead of time: exactly one of the two is set.
     * Errors are held, not thrown, so they surface in row order during processing.
     */
    private record ParsedRow(PaymentRequest request, ServiceError error) {}

    public Response reconcileBatch(Object payments) {
        if (!(payments instanceof List<?> rows) || rows.isEmpty()) {
            throw Parsing.invalidInput();
        }

        // 1. Parse every row OUTSIDE the lock, but don't throw yet: an idempotency
        //    conflict in an earlier row must win over a parse error in a later row.
        List<ParsedRow> parsed = new ArrayList<>(rows.size());
        for (Object row : rows) {
            try {
                parsed.add(new ParsedRow(Parsing.batchRow(row), null));
            } catch (ServiceError error) {
                parsed.add(new ParsedRow(null, error));
            }
        }

        lock.writeLock().lock();
        try {
            // 2. Work on a staged copy. Each row sees earlier rows' settlements and
            //    cache entries (so same-key replays inside the batch work).
            State staged = state.copy();
            List<Object> results = new ArrayList<>(parsed.size());
            for (ParsedRow row : parsed) {
                if (row.error() != null) {
                    throw row.error(); // first error in row order; staged is discarded
                }
                Response response = process(staged, row.request()); // may throw 409
                results.add(Values.row("status", response.status(), "body", response.body()));
            }

            // 3. Every row succeeded: commit by swapping in the staged state. Any throw
            //    above skipped this line, so the live state was never touched (rollback).
            state = staged;
            return new Response(200, Values.row("results", results));
        } finally {
            lock.writeLock().unlock();
        }
    }
}
