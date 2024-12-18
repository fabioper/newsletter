package com.github.fabioper.newsletter.domain.edition;

import java.util.UUID;

public record NoteId(UUID value) {
    public NoteId(String value) {
        this(UUID.fromString(value));
    }

    public NoteId() {
        this(UUID.randomUUID());
    }
}
