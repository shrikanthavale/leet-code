package com.shrikane.projects.paymentreconcilation;

import static com.shrikane.projects.paymentreconcilation.Values.*;

import java.util.*;

public class ReconciliationApp {
    private static Response error(int status, String code) {
        return new Response(status, row("error", code));
    }

    public static Response dispatch(
            ReconciliationService service, String method, String path, Object body, String key) {
        try {
            if (path.equals("/invoices")) {
                if (method.equals("GET")) {
                    return service.listInvoices();
                }
                if (!method.equals("POST")) {
                    return error(405, "method_not_allowed");
                }
                if (!(body instanceof Map<?, ?> map) || !(map.get("invoices") instanceof List<?>)) {
                    return error(400, "invalid_input");
                }
                return service.registerInvoices(map.get("invoices"));
            }
            if (path.equals("/payment-batches")) {
                if (!method.equals("POST")) {
                    return error(405, "method_not_allowed");
                }
                if (!(body instanceof Map<?, ?> map) || !map.keySet().equals(Set.of("payments"))) {
                    return error(400, "invalid_input");
                }
                return service.reconcileBatch(map.get("payments"));
            }
            if (path.equals("/payments")) {
                if (!method.equals("POST")) {
                    return error(405, "method_not_allowed");
                }
                if (!(body instanceof Map<?, ?> map) || !(map.get("payment") instanceof String)) {
                    return error(400, "invalid_input");
                }
                Object forgiveness = map.containsKey("forgiveness") ? map.get("forgiveness") : 0;
                if (!integer(forgiveness)) {
                    return error(400, "invalid_input");
                }
                return service.reconcilePayment(map.get("payment"), forgiveness, key);
            }
            return error(404, "not_found");
        } catch (ServiceError caughtError) {
            return error(caughtError.status, caughtError.code);
        } catch (IllegalArgumentException caughtError) {
            return error(400, "invalid_input");
        }
    }

    public static void main(String[] args) throws Exception {
        ReconciliationService service = new ReconciliationService();
        new HttpSupport(
                8080,
                request ->
                        new HttpSupport.Reply(
                                dispatch(
                                        service,
                                        request.method(),
                                        request.path(),
                                        request.body(),
                                        request.key())));
    }
}
