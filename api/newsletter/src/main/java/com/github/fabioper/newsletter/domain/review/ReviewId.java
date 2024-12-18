package com.github.fabioper.newsletter.domain.review;

import java.util.UUID;

public record ReviewId(UUID value) {
    public static ReviewId from(String value) {
        return new ReviewId(UUID.fromString(value));
    }

    public ReviewId() {
        this(UUID.randomUUID());
    }
}
