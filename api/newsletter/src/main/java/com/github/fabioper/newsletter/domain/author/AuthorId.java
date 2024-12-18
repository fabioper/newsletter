package com.github.fabioper.newsletter.domain.author;

import java.util.UUID;

public record AuthorId(UUID value) {
    public static AuthorId from(String value) {
        return new AuthorId(UUID.fromString(value));
    }

    public AuthorId() {
        this(UUID.randomUUID());
    }
}
