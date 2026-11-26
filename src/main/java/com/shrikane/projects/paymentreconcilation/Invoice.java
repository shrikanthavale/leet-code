package com.shrikane.projects.paymentreconcilation;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * One ledger entry. Immutable on purpose: settling an invoice produces a NEW record
 * instead of changing this one, so a shallow copy of the ledger map (used for batch
 * staging) can never share mutable state with the real ledger.
 */
record Invoice(
        String invoiceId,
        LocalDate dueDate,
        BigInteger amountCents,
        Status status,
        String settledByPaymentId) {

    enum Status {
        UNPAID,
        PAID
    }

    /** A freshly registered invoice: UNPAID and not settled by anyone. */
    static Invoice unpaid(String invoiceId, LocalDate dueDate, BigInteger amountCents) {
        return new Invoice(invoiceId, dueDate, amountCents, Status.UNPAID, null);
    }

    boolean isUnpaid() {
        return status == Status.UNPAID;
    }

    /** Returns the PAID version of this invoice; this instance is left untouched. */
    Invoice settledBy(String paymentId) {
        return new Invoice(invoiceId, dueDate, amountCents, Status.PAID, paymentId);
    }

    /**
     * The shape listInvoices returns. A brand-new map every call, so callers can mutate
     * it freely. LinkedHashMap because it allows the null settled_by_payment_id.
     */
    Map<String, Object> toRow() {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("invoice_id", invoiceId);
        row.put("due_date", dueDate.toString());
        row.put("amount_cents", amountCents);
        row.put("status", status.name());
        row.put("settled_by_payment_id", settledByPaymentId);
        return row;
    }
}
