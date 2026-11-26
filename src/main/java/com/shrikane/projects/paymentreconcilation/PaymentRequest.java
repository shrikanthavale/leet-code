package com.shrikane.projects.paymentreconcilation;

import java.math.BigInteger;

/**
 * One fully validated payment call: the parsed payment string plus its forgiveness and
 * idempotency key. Only Parsing.payment(...) creates these, so every instance is valid.
 *
 * @param paymentId audit label (NOT an idempotency key), trimmed, 1..64 code points
 * @param amount positive amount in cents
 * @param memo everything after the amount, commas preserved; "" when omitted
 * @param forgiveness tolerance for Tier 3 fuzzy matching; 0 disables it
 * @param key idempotency key, or null; whitespace is significant, so never trimmed
 * @param canonicalPayment UnicodeStrings.strip(raw payment): what retries are compared on
 */
record PaymentRequest(
        String paymentId,
        BigInteger amount,
        String memo,
        BigInteger forgiveness,
        String key,
        String canonicalPayment) {}
