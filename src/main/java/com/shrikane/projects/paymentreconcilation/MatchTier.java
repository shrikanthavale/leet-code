package com.shrikane.projects.paymentreconcilation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The matching tiers, in priority order. Declaration order IS the priority: firstMatch
 * walks values() top to bottom and the first tier that finds an invoice wins.
 * Each tier only ever considers UNPAID invoices.
 */
enum MatchTier {

    /** Tier 1: an invoice ID named in the memo after "paying for:" / "paying off:". */
    ID("id") {
        @Override
        Optional<Invoice> find(PaymentRequest payment, Map<String, Invoice> ledger) {
            // Markers are tried left to right; the first one naming an UNPAID invoice
            // wins, even if amounts differ. IDs are unique, so no tie-break is needed.
            for (String invoiceId : markedInvoiceIds(payment.memo())) {
                Invoice invoice = ledger.get(invoiceId);
                if (invoice != null && invoice.isUnpaid()) {
                    return Optional.of(invoice);
                }
            }
            return Optional.empty();
        }
    },

    /** Tier 2: invoice amount equals the payment amount exactly. */
    EXACT("exact") {
        @Override
        Optional<Invoice> find(PaymentRequest payment, Map<String, Invoice> ledger) {
            return best(payment, ledger.values(),
                    invoice -> invoice.amountCents().equals(payment.amount()));
        }
    },

    /** Tier 3: amount within [payment - forgiveness, payment + forgiveness]; off when 0. */
    FUZZY("fuzzy") {
        @Override
        Optional<Invoice> find(PaymentRequest payment, Map<String, Invoice> ledger) {
            if (payment.forgiveness().signum() == 0) {
                return Optional.empty();
            }
            return best(payment, ledger.values(),
                    invoice -> distance(invoice, payment).compareTo(payment.forgiveness()) <= 0);
        }
    };

    /** A successful match: which invoice, and which tier found it. */
    record Match(MatchTier tier, Invoice invoice) {}

    /** The value reported as "tier_used". */
    final String label;

    MatchTier(String label) {
        this.label = label;
    }

    abstract Optional<Invoice> find(PaymentRequest payment, Map<String, Invoice> ledger);

    /** Runs the tiers in priority order; the first tier that finds an invoice wins. */
    static Optional<Match> firstMatch(PaymentRequest payment, Map<String, Invoice> ledger) {
        for (MatchTier tier : values()) {
            Optional<Invoice> found = tier.find(payment, ledger);
            if (found.isPresent()) {
                return Optional.of(new Match(tier, found.get()));
            }
        }
        return Optional.empty();
    }

    // ---------------------------------------------------------------- amount tiers

    /** Shared by Tier 2 and 3: unpaid invoices passing the filter, best one by tie-break. */
    private static Optional<Invoice> best(
            PaymentRequest payment, Collection<Invoice> invoices, Predicate<Invoice> accepts) {
        Comparator<Invoice> tieBreak =
                Comparator.comparing(Invoice::dueDate)                              // 1. earliest due date
                        .thenComparing(invoice -> distance(invoice, payment))       // 2. closest amount
                        .thenComparing(Invoice::invoiceId, UnicodeStrings::compare); // 3. smallest ID
        return invoices.stream()
                .filter(Invoice::isUnpaid)
                .filter(accepts)
                .min(tieBreak);
    }

    /** abs(invoice_amount - payment_amount) */
    private static BigInteger distance(Invoice invoice, PaymentRequest payment) {
        return invoice.amountCents().subtract(payment.amount()).abs();
    }

    // ---------------------------------------------------------------- memo markers

    private static final List<String> MARKERS = List.of("paying for:", "paying off:");

    /**
     * Every invoice ID named after a marker, in left-to-right order of the markers.
     * The ID runs to the next comma (or end of memo) and is trimmed, casing preserved.
     */
    static List<String> markedInvoiceIds(String memo) {
        List<String> ids = new ArrayList<>();
        for (int start = 0; start < memo.length(); start++) {
            for (String marker : MARKERS) {
                if (markerAt(memo, start, marker)) {
                    int idStart = start + marker.length();
                    int comma = memo.indexOf(',', idStart);
                    int idEnd = comma < 0 ? memo.length() : comma;
                    ids.add(UnicodeStrings.strip(memo.substring(idStart, idEnd)));
                }
            }
        }
        return ids;
    }

    /** Does memo contain marker at this position, case-insensitively (Python-regex rules)? */
    private static boolean markerAt(String memo, int start, String marker) {
        if (start + marker.length() > memo.length()) {
            return false;
        }
        for (int offset = 0; offset < marker.length(); offset++) {
            if (!sameIgnoringCase(marker.charAt(offset), memo.charAt(start + offset))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Case-insensitive comparison of one marker character. Every marker character is
     * ASCII. The letter i is special: Python's re.IGNORECASE also treats dotted capital
     * İ (U+0130) and dotless ı (U+0131) as i, which no plain lower/upper-casing does.
     */
    private static boolean sameIgnoringCase(char markerChar, char memoChar) {
        if (markerChar == 'i') {
            return memoChar == 'i' || memoChar == 'I' || memoChar == 'İ' || memoChar == 'ı';
        }
        return memoChar == markerChar || memoChar == Character.toUpperCase(markerChar);
    }
}
