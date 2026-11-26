package com.shrikane.projects.paymentreconcilation;

import java.math.BigInteger;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Turns raw input strings into validated values. Pure functions: no state, no locks.
 * Every failure is reported as ServiceError(400, "invalid_input").
 */
final class Parsing {
    /** The field delimiter for both invoice and payment strings: comma + one space. */
    static final String DELIMITER = ", ";

    /** [0-9] is ASCII-only in Java regex (unlike \d with UNICODE_CHARACTER_CLASS). */
    private static final Pattern ASCII_DIGITS = Pattern.compile("[0-9]+");

    private static final Pattern ISO_DATE = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

    private static final int MAX_ID_LENGTH = 64;

    private Parsing() {}

    static ServiceError invalidInput() {
        return new ServiceError(400, "invalid_input");
    }

    /** Parses "invoice-id, due-date, amount" into an UNPAID invoice. */
    static Invoice invoice(Object raw) {
        if (!(raw instanceof String text)) {
            throw invalidInput();
        }
        // limit -1 keeps trailing empty parts, so "a, 2026-01-01, 5, " is 4 parts, not 3.
        String[] parts = text.split(DELIMITER, -1);
        if (parts.length != 3) {
            throw invalidInput();
        }
        String invoiceId = identifier(parts[0]);
        LocalDate dueDate = date(parts[1]);
        BigInteger amount = positiveAmount(parts[2]);
        return Invoice.unpaid(invoiceId, dueDate, amount);
    }

    /**
     * Parses one payment call: "payment-id, amount, memo..." plus forgiveness and key.
     * forgiveness and key arrive as Object because callers may send anything.
     */
    static PaymentRequest payment(Object raw, Object forgiveness, Object key) {
        if (!(raw instanceof String text)) {
            throw invalidInput();
        }
        String[] parts = text.split(DELIMITER, -1);
        if (parts.length < 2) { // need at least id and amount; memo is optional
            throw invalidInput();
        }
        String paymentId = identifier(parts[0]);
        BigInteger amount = positiveAmount(parts[1]);
        // Re-join everything after the amount, so "a, b, c" in the memo survives intact.
        String memo = parts.length == 2
                ? ""
                : String.join(DELIMITER, java.util.Arrays.copyOfRange(parts, 2, parts.length));

        return new PaymentRequest(
                paymentId,
                amount,
                memo,
                forgiveness(forgiveness),
                key(key),
                UnicodeStrings.strip(text));
    }

    private static final java.util.Set<String> BATCH_ROW_KEYS =
            java.util.Set.of("payment", "forgiveness", "key");

    /**
     * One reconcileBatch entry: a map with "payment" and optional "forgiveness"/"key",
     * defaulting like the single-call overload. Unknown keys are invalid. Only reads
     * the map, never mutates it.
     */
    static PaymentRequest batchRow(Object raw) {
        if (!(raw instanceof java.util.Map<?, ?> row) || !row.containsKey("payment")) {
            throw invalidInput();
        }
        for (Object name : row.keySet()) {
            // instanceof first: Set.of(...).contains(null) would throw NPE
            if (!(name instanceof String text) || !BATCH_ROW_KEYS.contains(text)) {
                throw invalidInput();
            }
        }
        Object forgiveness = row.containsKey("forgiveness") ? row.get("forgiveness") : 0;
        Object key = row.containsKey("key") ? row.get("key") : null;
        return payment(row.get("payment"), forgiveness, key);
    }

    /** Integer types only (Boolean, Double, String, null are all invalid), and >= 0. */
    static BigInteger forgiveness(Object raw) {
        if (!Values.integer(raw)) {
            throw invalidInput();
        }
        BigInteger value = Values.number(raw);
        if (value.signum() < 0) {
            throw invalidInput();
        }
        return value;
    }

    /** null (no idempotency) or a string of 1..64 code points. NOT trimmed. */
    static String key(Object raw) {
        if (raw == null) {
            return null;
        }
        if (!(raw instanceof String text)) {
            throw invalidInput();
        }
        int length = text.codePointCount(0, text.length());
        if (length < 1 || length > MAX_ID_LENGTH) {
            throw invalidInput();
        }
        return text;
    }

    /** Trims, then requires 1..64 Unicode code points (not Java chars). */
    static String identifier(String raw) {
        String value = UnicodeStrings.strip(raw);
        int length = value.codePointCount(0, value.length());
        if (length < 1 || length > MAX_ID_LENGTH) {
            throw invalidInput();
        }
        return value;
    }

    /** Trims, then requires ASCII digits only (no sign, no underscore) and a value > 0. */
    static BigInteger positiveAmount(String raw) {
        String value = UnicodeStrings.strip(raw);
        if (!ASCII_DIGITS.matcher(value).matches()) {
            throw invalidInput();
        }
        BigInteger amount = new BigInteger(value); // leading zeros are fine: "0100" -> 100
        if (amount.signum() <= 0) {
            throw invalidInput();
        }
        return amount;
    }

    /** Trims, then requires strict YYYY-MM-DD that is a real calendar date. */
    static LocalDate date(String raw) {
        String value = UnicodeStrings.strip(raw);
        if (!ISO_DATE.matcher(value).matches()) {
            throw invalidInput();
        }
        int year = Integer.parseInt(value.substring(0, 4));
        int month = Integer.parseInt(value.substring(5, 7));
        int day = Integer.parseInt(value.substring(8, 10));
        if (year < 1) { // Python's date has no year 0; LocalDate would accept it
            throw invalidInput();
        }
        try {
            return LocalDate.of(year, month, day); // rejects 2026-02-30, month 13, etc.
        } catch (DateTimeException invalidDate) {
            throw invalidInput();
        }
    }
}
