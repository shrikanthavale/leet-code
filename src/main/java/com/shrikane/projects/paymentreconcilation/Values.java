package com.shrikane.projects.paymentreconcilation;

import java.math.BigInteger;
import java.util.*;

public final class Values {
    private Values() {}

    public static Map<String, Object> row(Object... pairs) {
        Map<String, Object> value = new LinkedHashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            value.put((String) pairs[i], pairs[i + 1]);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <ValueType> ValueType copy(ValueType value) {
        if (value instanceof Map<?, ?> map) {
            Map<Object, Object> result = new LinkedHashMap<>();
            map.forEach((key, item) -> result.put(key, copy(item)));
            return (ValueType) result;
        }
        if (value instanceof Collection<?> items) {
            List<Object> result = new ArrayList<>();
            for (Object item : items) {
                result.add(copy(item));
            }
            return (ValueType) result;
        }
        if (value instanceof byte[] bytes) {
            return (ValueType) bytes.clone();
        }
        return value;
    }

    public static boolean integer(Object value) {
        return value instanceof Byte
                || value instanceof Short
                || value instanceof Integer
                || value instanceof Long
                || value instanceof BigInteger;
    }

    public static BigInteger number(Object value) {
        if (!integer(value)) {
            throw new IllegalArgumentException("expected integer");
        }
        return value instanceof BigInteger numberValue
                ? numberValue
                : BigInteger.valueOf(((Number) value).longValue());
    }

    public static BigInteger amount(Object value, String code) {
        if (!integer(value) || number(value).signum() <= 0) {
            throw new ServiceError(400, code);
        }
        return number(value);
    }

    public static void require(boolean condition, int status, String code) {
        if (!condition) {
            throw new ServiceError(status, code);
        }
    }

    public static boolean text(Object value) {
        return value instanceof String textValue && !UnicodeStrings.strip(textValue).isEmpty();
    }
}
