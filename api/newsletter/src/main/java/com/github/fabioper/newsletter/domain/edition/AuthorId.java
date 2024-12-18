package com.github.fabioper.newsletter.domain.edition;

import java.util.UUID;

public record AuthorId(UUID value) {
    public AuthorId(String value) {
        this(UUID.fromString(value));
    }

    public AuthorId() {
        this(UUID.randomUUID());
    }
}
