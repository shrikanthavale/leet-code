
## Prerequisites

Mode: Completion.

Use JDK 21 and Maven.

---

## Scenario

ReconcileHub needs a resilient ledger reconciliation engine that accurately settles invoices when clients submit bank wires, payment retry requests, and concurrent transactions. Complete the service behind the supplied HTTP adapter.

---

## Files

Edit `src/main/java/project/ReconciliationService.java` and `REFLECTION.essay`. Keep the public names, signatures, and supplied fixed files unchanged. You may add helper modules and your own regression tests.

---

## Public API

```java
ReconciliationService()
Response registerInvoices(Object invoices)
Response reconcilePayment(Object payment)
Response reconcilePayment(Object payment, Object forgiveness, Object key)
Response listInvoices()
Response reconcileBatch(Object payments)
```

Import `project.ReconciliationService`, `java.util.*` and `java.math.BigInteger`. `project.ServiceError` exposes integer `status` and string `code`; `(status, code)` below refers to that exception, not a returned tuple, unless a success tuple is explicitly shown.

Java types: integer inputs accept `Byte`, `Short`, `Integer`, `Long` or `BigInteger`, never booleans or floating-point values. `Object` parameters allow invalid input types to reach validation. JSON-style `{...}` denotes a map; a successful `(status, body)` response denotes the fixed `Response` record, accessed through `status()` and `body()`. Count string lengths in Unicode code points. Where trimming, case folding or string ordering is required, use the fixed `UnicodeStrings.strip`, `casefold` and `compare` helpers, which follow Python 3.11 rules.

Invoice and payment batch arguments must be nonempty `List<?>` values. Invoice rows are strings; payment batch entries are maps with the documented keys. The payment overload without optional arguments uses forgiveness=0 and key=null. Money in returned invoice rows uses `BigInteger`; status codes and registered counts use `Integer`. Use the supplied Unicode helpers for stripping and code-point ordering.

---

## Requirements

### 1. Invoice batch registration

* `invoices` is a list of strings, each formatted as `"invoice-id, due-date, amount"`.
* Split each entry on the delimiter `", "` into exactly three parts, `invoice_id`, `due_date` and `amount`, and trim surrounding whitespace from each part before validating it.
* `invoice_id` must be a non-empty string of length 1..64 (surrounding whitespace trimmed).
* `due_date` must be a valid calendar date formatted strictly as `YYYY-MM-DD` with ASCII digits.
* `amount` must be a positive integer in cents (greater than zero), written using ASCII digits `0`–`9` only (leading zeros are allowed; signs, underscores and Unicode digits are invalid).
* Validate invoice rows in input order, including duplicates within the batch; after all rows are valid, check for IDs already in the ledger. The first encountered error wins.
* Empty list, malformed strings, invalid date strings, or non-positive amounts raise `(400, "invalid_input")`.
* `invoice_id` values must be unique across the service instance and within the batch. A duplicate ID raises `(409, "duplicate_invoice")`.
* Batch registration is atomic: if any item fails validation or introduces a duplicate ID, no invoices from the batch are stored.
* On success, every invoice starts with status `"UNPAID"` and `settled_by_payment_id: null`. Returns `(201, {"registered_count": count})`.
* Successive calls append to the existing ledger without clearing previously registered invoices.

### 2. Payment input parsing

* `payment` is a string formatted as `"payment-id, amount, memo"`. Split on `", "`.
* `payment_id`: non-empty string of length 1..64 (surrounding whitespace trimmed).
* `amount`: surrounding whitespace trimmed, then a positive integer in cents (greater than zero) with the same ASCII-digit rules as invoice amounts.
* `memo`: all remaining text re-joined with `", "` (or empty string if omitted). Commas within `memo` are preserved.
* `forgiveness` defaults to `0` and must be a non-negative integer (at least zero); booleans are invalid.
* `key` is `null` or a string of length 1..64 (whitespace is significant).
* Malformed payment strings, non-positive payment amounts, negative forgiveness, or invalid key types raise `(400, "invalid_input")`.

### 3. Progressive matching priority

Incoming payments match **only** against invoices currently marked `"UNPAID"`. Apply matching rules strictly in descending priority order:

1. **Tier 1 — Explicit ID match:** Scan `memo` case-insensitively for the marker `paying for:` or `paying off:`. If present, extract the invoice ID following the marker up to the next comma or end of string, stripped of surrounding whitespace (preserve its original casing). If an `UNPAID` invoice has that ID, it matches immediately, even if amounts differ. Scan markers from left to right and use the first ID that names an unpaid invoice. No space is required after the colon, and IDs may contain spaces. If no marker names an unpaid invoice, fall through to Tier 2.
2. **Tier 2 — Exact amount match:** If no ID matches, match `UNPAID` invoices whose `amount` equals the payment `amount` exactly.
3. **Tier 3 — Fuzzy amount match:** Active only when `forgiveness > 0`. Match `UNPAID` invoices whose amount lies within the inclusive range `[payment_amount - forgiveness, payment_amount + forgiveness]`.

Within whichever tier produces candidates, break ties using this order:

1. Earliest `due_date` (ascending chronological order).
2. Smallest absolute difference to payment amount (`abs(invoice_amount - payment_amount)`).
3. Lexicographically smallest `invoice_id` (Unicode code-point order).

### 4. Settlement and return values

* **Matched settlement:** If an invoice matches, transition its status to `"PAID"`, set its `settled_by_payment_id` to `payment_id`, and return `(201, {"status": "MATCHED", "payment_id": payment_id, "settled_invoice_id": invoice_id, "tier_used": tier, "message": "Payment {payment-id} paid {amount} for invoice {invoice-id} due on {date}"})`. Valid `tier_used` values are `"id"`, `"exact"`, or `"fuzzy"`. `{amount}` is the parsed payment amount.
* **Unmatched settlement:** If no tier matches, invoices remain unchanged. Return `(200, {"status": "UNMATCHED", "payment_id": payment_id, "settled_invoice_id": null, "tier_used": null, "message": "Payment {payment-id} could not be matched to any invoice"})`.

### 5. Idempotency and retries

* Idempotency scope is `key` per service instance.
* A request without a key (`key=null`) always evaluates against the current open ledger, even if its payment ID was used before. Payment IDs are audit labels, not implicit idempotency keys. Validate the request before checking its key.
* Reusing a key with the exact same canonical payload (`UnicodeStrings.strip(payment)`, `forgiveness`) returns `(200, cached_payload)` with the exact response map from the original attempt, even if the settled invoice is now `"PAID"` or new invoices have been registered.
* Replayed calls do not re-run matching logic, do not settle additional invoices, and do not alter state. Unmatched results are cached too.
* Reusing a key with a different canonical payment text or `forgiveness` raises `(409, "idempotency_conflict")`.
* Concurrent requests using the same key produce exactly one settlement attempt; all duplicate requests return `200` with the identical payload.

### 6. Listing and concurrency

* `listInvoices` returns `(200, list)` with all registered invoices across all batches; each item has exactly `{invoice_id, due_date, amount_cents, status, settled_by_payment_id}`.
* Invoices are ordered deterministically by `due_date` ascending; ties are broken by `invoice_id` ascending.
* State is kept in memory for one service instance; disk persistence is not required.
* All public methods must be thread-safe.
* Concurrent payments targeting the same matching invoice must settle it at most once; all competing payments must evaluate against remaining `UNPAID` invoices or return `"UNMATCHED"`.
* All returned objects and lists must be detached snapshots; mutating returned structures or input collections must not corrupt internal service state.

### 7. HTTP contract

* HTTP: `POST /invoices`; `POST /payments`; `GET /invoices`; `POST /payment-batches`.
* `POST /payment-batches` accepts exactly `{"payments": [...]}`. Each entry follows the batch API below; per-entry keys belong in the body. An `Idempotency-Key` header does not apply to this route.
* `POST /invoices` body is `{"invoices": ["INV-1, 2026-03-01, 100", ...]}`. Success is `201`.
* `POST /payments` body is `{"payment": "pay_1, 100, memo...", "forgiveness": 0}` with an optional `Idempotency-Key` header.
* `GET /invoices` returns `200` with the ordered array of all invoices.
* Errors are `{error: code}`; the fixed adapter rejects malformed JSON with `400 invalid_json`, unknown routes with `404 not_found`, and unsupported methods with `405 method_not_allowed`.

### 8. Transactional payment batches

`reconcileBatch(payments)` accepts a nonempty list of maps. Each map must contain `payment` and may contain `forgiveness` and `key`, with the same defaults and validation as `reconcilePayment`. Extra keys and invalid row shapes raise `(400, "invalid_input")`.

Process rows in order against one staged ledger and idempotency cache. Each row sees earlier rows' settlements and cached responses, including same-key replays within this batch. Stop at the first error in row order. On any validation or idempotency conflict, roll back every settlement and cache entry created by this batch; retain state and keys that existed before it. An unmatched payment is a successful result, not a batch failure. Return `(200, {"results": [{"status": row_status, "body": row_payload}, ...]})`, preserving each individual call's status and payload.

All methods, including listing and single payments, must observe batches atomically. Concurrent batches must have an outcome equivalent to executing them in some serial order. Do not mutate request maps; nested batch results must be detached from the cache and from each other.

### Example

```java
var service = new ReconciliationService();
service.registerInvoices(List.of("INV A, 2026-03-01, 100", "INV B, 2026-03-02, 100"));
var result = service.reconcileBatch(List.of(
    Map.of("payment", "p1, 1, paying for:INV A", "key", "retry-1"),
    Map.of("payment", "p1, 1, paying for:INV A", "key", "retry-1"),
    Map.of("payment", "p2, 100")));
// result.status() == 200
// Row statuses: [201, 200, 201]; matched invoices: ["INV A", "INV A", "INV B"].
// Row one matches by ID despite the amount difference. Row two replays it.
```

A batch containing `{"payment": "p3, 100", "key": "new-key"}` followed by `{"payment": "invalid"}` raises `400 invalid_input`. Neither its first settlement attempt nor `new-key` survives. Earlier successful calls remain committed.

---

## Completion checklist

Each task is required.

1. Parse invoice CSV strings and atomically register batches into the open ledger.
2. Ingest payment strings, preserving commas inside memos and extracting ID markers case-insensitively.
3. Implement the three-tier matching priority (`id` > `exact` > `fuzzy`) with multi-level tie-breaking.
4. Atomically transition settled invoices to `"PAID"` and isolate them from future matches.
5. Handle retries and concurrent submissions safely under the `key` scope.
6. Implement transactional payment batches with ledger and replay-cache rollback.
7. Provide deterministic invoice listing, and verify the service through the supplied HTTP adapter.

---

## Estimated time

60 minutes, including investigation, implementation, regression testing, and the reflection answers.

---

## Running the tests

Click **Run** in the IDE to run the public tests.

Some tests will fail against the starter code. After making changes, check the test results as well as whether the process starts. The visible tests cover part of the contract; your implementation must satisfy all the rules above. Tests have time limits and use temporary state.

After completing the code, fill in each question’s `answer` field in `REFLECTION.essay`. The questions ask you to explain your approach and discuss possible future changes. You do not need to implement the proposed extensions.

The marker uses Python-regex-compatible case matching for the letter `i`: `i`, `I`, `İ` and `ı` are equivalent. Thus `PAYİNG FOR:` and `payıng off:` are valid markers. This applies only to the marker; invoice IDs remain case-sensitive. Ordinary case folding of the entire memo is insufficient for this rule.

### Optional: run the server yourself

**Run** does not need a running server.

```bash
bash .theia/run_app.sh

```

Use the documented routes at `http://127.0.0.1:8080`. The adapter is already complete.
