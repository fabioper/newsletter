package com.github.fabioper.newsletter.domain.editor;

import java.util.UUID;

public record EditorId(UUID value) {
    public static EditorId from(String value) {
        return new EditorId(UUID.fromString(value));
    }

    public EditorId() {
        this(UUID.randomUUID());
    }
}
