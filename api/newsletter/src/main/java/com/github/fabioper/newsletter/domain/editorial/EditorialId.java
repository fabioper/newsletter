package com.github.fabioper.newsletter.domain.editorial;

import java.util.UUID;

public record EditorialId(UUID value) {
    public static EditorialId from(String value) {
        return new EditorialId(UUID.fromString(value));
    }

    public EditorialId() {
        this(UUID.randomUUID());
    }
}
