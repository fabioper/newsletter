package com.github.fabioper.newsletter.domain.shared;

import java.util.Objects;

public class Guard {
    public static void againstNullOrEmpty(Object value, String message) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(message);
        }

        if (value instanceof String && ((String) value).isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
