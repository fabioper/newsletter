package com.github.fabioper.newsletter.domain.reviewer;

import java.util.UUID;

public record ReviewerId(UUID value) {
    public static ReviewerId from(String value) {
        return new ReviewerId(UUID.fromString(value));
    }

    public ReviewerId() {
        this(UUID.randomUUID());
    }
}
