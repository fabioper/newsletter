package com.github.fabioper.newsletter.domain.edition;

import java.util.UUID;

public record EditorId(UUID value) {
    public EditorId(String value) {
        this(UUID.fromString(value));
    }

    public EditorId() {
        this(UUID.randomUUID());
    }
}
