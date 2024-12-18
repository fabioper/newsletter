package com.github.fabioper.newsletter.domain.category;

import java.util.UUID;

public record CategoryId(UUID value) {
    public CategoryId(String value) {
        this(UUID.fromString(value));
    }

    public CategoryId() {
        this(UUID.randomUUID());
    }
}
