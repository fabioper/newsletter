package com.github.fabioper.newsletter.domain.category;

import java.util.UUID;

public record CategoryId(UUID value) {
    public static CategoryId from(String value) {
        return new CategoryId(UUID.fromString(value));
    }

    public CategoryId() {
        this(UUID.randomUUID());
    }
}
