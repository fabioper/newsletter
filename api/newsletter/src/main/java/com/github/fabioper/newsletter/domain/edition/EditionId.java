package com.github.fabioper.newsletter.domain.edition;

import java.util.UUID;

public record EditionId(UUID value) {
    public EditionId(String id) {
        this(UUID.fromString(id));
    }

    public EditionId() {
        this(UUID.randomUUID());
    }
}
