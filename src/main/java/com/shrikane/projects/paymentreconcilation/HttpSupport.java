package com.shrikane.projects.paymentreconcilation;

import static com.shrikane.projects.paymentreconcilation.Values.*;

import com.fasterxml.jackson.databind.*;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public final class HttpSupport implements AutoCloseable {
    public record Request(
            String method,
            String path,
            String user,
            Object body,
            Map<String, String> query,
            String key,
            String baseUrl) {}

    public record Reply(int status, Object body, Map<String, String> headers) {
        public Reply(Response response) {
            this(response.status(), response.body(), Map.of());
        }

        public Reply(int status, Object body) {
            this(status, body, Map.of());
        }
    }

    @FunctionalInterface
    public interface Router {
        Reply dispatch(Request request) throws Exception;
    }

    private static final ObjectMapper JSON =
            new ObjectMapper()
                    .enable(
                            DeserializationFeature.USE_BIG_INTEGER_FOR_INTS,
                            DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
    private final HttpServer server;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public HttpSupport(int port, Router router) throws IOException {
        server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.setExecutor(pool);
        server.createContext(
                "/",
                exchange -> {
                    try {
                        String query = exchange.getRequestURI().getRawQuery();
                        Map<String, String> params = new LinkedHashMap<>();
                        if (query != null) {
                            for (String pair : query.split("&")) {
                                String[] parts = pair.split("=", 2);
                                params.put(
                                        decode(parts[0]),
                                        parts.length == 2 ? decode(parts[1]) : "");
                            }
                        }
                        byte[] bytes = exchange.getRequestBody().readAllBytes();
                        Object body = new LinkedHashMap<>();
                        if (bytes.length > 0) {
                            try {
                                body = JSON.readValue(bytes, Object.class);
                            } catch (IOException invalid) {
                                write(exchange, new Reply(400, row("error", "invalid_json")));
                                return;
                            }
                        }
                        String host = exchange.getRequestHeaders().getFirst("Host");
                        Reply reply =
                                router.dispatch(
                                        new Request(
                                                exchange.getRequestMethod(),
                                                exchange.getRequestURI().getPath(),
                                                exchange.getRequestHeaders().getFirst("X-User-ID"),
                                                body,
                                                params,
                                                exchange.getRequestHeaders()
                                                        .getFirst("Idempotency-Key"),
                                                "http://"
                                                        + (host == null
                                                                ? "127.0.0.1:" + port
                                                                : host)));
                        write(exchange, reply);
                    } catch (Exception error) {
                        write(exchange, new Reply(500, row("error", "internal_error")));
                    } finally {
                        exchange.close();
                    }
                });
        server.start();
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static void write(HttpExchange exchange, Reply reply) throws IOException {
        reply.headers().forEach((key, value) -> exchange.getResponseHeaders().set(key, value));
        if (reply.status() == 204) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }
        byte[] bytes = JSON.writeValueAsBytes(reply.body());
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        if (exchange.getRequestMethod().equals("HEAD")) {
            exchange.getResponseHeaders().set("Content-Length", Integer.toString(bytes.length));
            exchange.sendResponseHeaders(reply.status(), -1);
            return;
        }
        exchange.sendResponseHeaders(reply.status(), bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    public int port() {
        return server.getAddress().getPort();
    }

    public void close() {
        server.stop(0);
        pool.close();
    }
}
