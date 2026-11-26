package com.shrikane.projects.paymentreconcilation;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.*;

import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

public final class ContractCases {
    private static final ObjectMapper JSON =
            new ObjectMapper().enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);

    private ContractCases() {}

    private static String camel(String input) {
        StringBuilder result = new StringBuilder();
        boolean upper = false;
        for (char character : input.toCharArray()) {
            if (character == '_') {
                upper = true;
            } else {
                result.append(upper ? Character.toUpperCase(character) : character);
                upper = false;
            }
        }
        return result.toString();
    }

    private static Object convert(JsonNode node, Type type) throws Exception {
        if (type instanceof ParameterizedType filePath
                && filePath.getRawType() == List.class
                && filePath.getActualTypeArguments()[0] instanceof Class<?> itemType
                && itemType.isRecord()) {
            RecordComponent[] components = itemType.getRecordComponents();
            Class<?>[] types =
                    Arrays.stream(components)
                            .map(RecordComponent::getType)
                            .toArray(Class<?>[]::new);
            List<Object> items = new ArrayList<>();
            for (JsonNode item : node) {
                Object[] args = new Object[components.length];
                for (int i = 0; i < args.length; i++) {
                    args[i] = convert(item.get(i), components[i].getGenericType());
                }
                items.add(itemType.getConstructor(types).newInstance(args));
            }
            return items;
        }
        return JSON.convertValue(node, JSON.getTypeFactory().constructType(type));
    }

    private static JsonNode normalized(Object value) throws Exception {
        if (value instanceof Response response) {
            value = Arrays.asList(response.status(), response.body());
        }
        return JSON.readTree(JSON.writeValueAsString(value));
    }

    public static void run(Class<?> type, String resource, int index) throws Exception {
        JsonNode scenario;
        try (var input = ContractCases.class.getResourceAsStream(resource)) {
            assertNotNull(input, resource);
            scenario = JSON.readTree(input).get(index);
        }
        Map<Integer, Object> instances = new HashMap<>();
        int step = 0;
        Path directory = Files.createTempDirectory("contract-databases-");
        Map<String, String> databases = new HashMap<>();
        try {
            for (JsonNode event : scenario.get("events")) {
                step++;
                String name = event.get("method").asText();
                int id = event.get("id").asInt();
                JsonNode args = event.get("args");
                Executable executable;
                if (name.equals("new")) {
                    executable =
                            Arrays.stream(type.getConstructors())
                                    .filter(
                                            localConstructor ->
                                                    localConstructor.getParameterCount()
                                                            == args.size())
                                    .findFirst()
                                    .orElseThrow(
                                            () ->
                                                    new AssertionError(
                                                            "missing constructor: " + args));
                } else {
                    executable =
                            Arrays.stream(type.getMethods())
                                    .filter(
                                            method ->
                                                    method.getName().equals(camel(name))
                                                            && method.getParameterCount()
                                                                    == args.size())
                                    .findFirst()
                                    .orElseThrow(
                                            () ->
                                                    new AssertionError(
                                                            "missing method " + name + " " + args));
                }
                Object[] converted = new Object[args.size()];
                Type[] parameterTypes = executable.getGenericParameterTypes();
                for (int i = 0; i < converted.length; i++) {
                    converted[i] = convert(args.get(i), parameterTypes[i]);
                }
                if (type.getSimpleName().equals("WatchlistService")
                        && name.equals("new")
                        && !Objects.equals(converted[0], ":memory:")) {
                    String original = (String) converted[0];
                    converted[0] =
                            databases.computeIfAbsent(
                                    original,
                                    path ->
                                            directory
                                                    .resolve("db-" + databases.size() + ".sqlite")
                                                    .toString());
                }
                Object output = null;
                Throwable error = null;
                try {
                    if (executable instanceof Constructor<?> constructor) {
                        instances.put(id, constructor.newInstance(converted));
                    } else {
                        output = ((Method) executable).invoke(instances.get(id), converted);
                    }
                } catch (InvocationTargetException caught) {
                    error = caught.getCause();
                }
                String context = "step " + step + " " + name + " " + args;
                if (event.has("error")) {
                    assertNotNull(error, context + " should fail");
                    JsonNode expected = event.get("error");
                    if (expected.has("code")) {
                        assertInstanceOf(ServiceError.class, error, context);
                        ServiceError serviceError = (ServiceError) error;
                        assertEquals(expected.get("code").asText(), serviceError.code, context);
                        assertEquals(expected.get("status").asInt(), serviceError.status, context);
                    } else {
                        assertEquals(
                                expected.get("type").asText(),
                                error.getClass().getSimpleName(),
                                context);
                    }
                } else {
                    if (error != null) {
                        throw new AssertionError(context, error);
                    }
                    assertEquals(event.get("result"), normalized(output), context);
                }
            }
        } finally {
            for (Object instance : instances.values()) {
                if (instance instanceof AutoCloseable resourceToClose) {
                    resourceToClose.close();
                }
            }
            try (var paths = Files.walk(directory)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.deleteIfExists(path);
                }
            }
        }
    }
}
