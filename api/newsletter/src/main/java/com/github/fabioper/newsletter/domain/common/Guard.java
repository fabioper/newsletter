package com.github.fabioper.newsletter.domain.common;

public class Guard {
    public static void againstNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
