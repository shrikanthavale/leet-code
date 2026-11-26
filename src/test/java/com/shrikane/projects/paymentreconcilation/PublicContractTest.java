package com.shrikane.projects.paymentreconcilation;

import org.junit.jupiter.api.*;

@DisplayName("ReconciliationService behavior")
@Timeout(15)
class PublicContractTest {
    @Test
    @DisplayName("Atomically registers an initial batch of open invoices.")
    void test_register_invoices() throws Exception {
        ContractCases.run(ReconciliationService.class, "/project/public-cases.json", 0);
    }

    @Test
    @DisplayName("Resolves Tier 1 marker even when the payment amount differs.")
    void test_tier1_id_match() throws Exception {
        ContractCases.run(ReconciliationService.class, "/project/public-cases.json", 1);
    }

    @Test
    @DisplayName("Resolves Tier 2 exact match and breaks date ties by earliest date.")
    void test_tier2_exact_amount() throws Exception {
        ContractCases.run(ReconciliationService.class, "/project/public-cases.json", 2);
    }

    @Test
    @DisplayName("Resolves Tier 3 fuzzy match within inclusive forgiveness tolerance.")
    void test_tier3_fuzzy_match() throws Exception {
        ContractCases.run(ReconciliationService.class, "/project/public-cases.json", 3);
    }

    @Test
    @DisplayName("A later invalid payment undoes the earlier settlement in the same batch.")
    void test_transactional_batch() throws Exception {
        ContractCases.run(ReconciliationService.class, "/project/public-cases.json", 4);
    }

    @Test
    @DisplayName("Returns 200 with UNMATCHED status when no invoice matches.")
    void test_unmatched_payment() throws Exception {
        ContractCases.run(ReconciliationService.class, "/project/public-cases.json", 5);
    }

    @Test
    @DisplayName("Malformed or trailing JSON is rejected before dispatch")
    void rejectsInvalidJson() throws Exception {
        var dispatched = new java.util.concurrent.atomic.AtomicInteger();
        try (var server =
                        new HttpSupport(
                                0,
                                request -> {
                                    dispatched.incrementAndGet();
                                    return new HttpSupport.Reply(200, java.util.Map.of("ok", true));
                                });
                var client = java.net.http.HttpClient.newHttpClient()) {
            for (String body : java.util.List.of("{", "{} {}", "{} trailing")) {
                var request =
                        java.net.http.HttpRequest.newBuilder(
                                        java.net.URI.create(
                                                "http://127.0.0.1:" + server.port() + "/"))
                                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                                .build();
                var response =
                        client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
                org.junit.jupiter.api.Assertions.assertEquals(400, response.statusCode());
                org.junit.jupiter.api.Assertions.assertTrue(
                        response.body().contains("invalid_json"));
            }
            org.junit.jupiter.api.Assertions.assertEquals(0, dispatched.get());
        }
    }
}
