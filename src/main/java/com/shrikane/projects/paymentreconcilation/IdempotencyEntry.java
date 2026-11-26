package com.shrikane.projects.paymentreconcilation;

import java.math.BigInteger;
import java.util.Map;

/**
 * What the service remembers for one idempotency key: which request it was, and the
 * exact response body we gave. Immutable, so shallow copies of the cache are safe.
 *
 * @param fingerprint identifies the request; the ONLY thing compared on a retry
 * @param body the original response body; a private copy nobody outside can reach
 */
record IdempotencyEntry(Fingerprint fingerprint, Map<String, Object> body) {

    /**
     * The readme's "canonical payload". Record equals() compares exactly these two
     * fields, so the response body is never part of the comparison.
     */
    record Fingerprint(String canonicalPayment, BigInteger forgiveness) {
        static Fingerprint of(PaymentRequest request) {
            return new Fingerprint(request.canonicalPayment(), request.forgiveness());
        }
    }

    /** Stores a private deep copy, so later changes to the caller's map can't reach us. */
    static IdempotencyEntry of(PaymentRequest request, Map<String, Object> body) {
        return new IdempotencyEntry(Fingerprint.of(request), Values.copy(body));
    }

    boolean sameRequestAs(PaymentRequest request) {
        return fingerprint.equals(Fingerprint.of(request));
    }
}
