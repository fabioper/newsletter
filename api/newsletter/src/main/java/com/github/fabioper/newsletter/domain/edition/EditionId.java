package com.github.fabioper.newsletter.domain.edition;

import java.util.UUID;

public record EditionId(UUID value) {
    public static EditionId from(String id) {
        return new EditionId(UUID.fromString(id));
    }

    public EditionId() {
        this(UUID.randomUUID());
    }
}
