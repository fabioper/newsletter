package com.github.fabioper.newsletter.domain.editorial;

import java.util.UUID;

public record EditorialId(UUID value) {
    public EditorialId(String value) {
        this(UUID.fromString(value));
    }

    public EditorialId() {
        this(UUID.randomUUID());
    }
}
