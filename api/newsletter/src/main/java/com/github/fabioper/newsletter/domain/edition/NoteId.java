package com.github.fabioper.newsletter.domain.edition;

import java.util.UUID;

public record NoteId(UUID value) {
    public static NoteId from(String value) {
        return new NoteId(UUID.fromString(value));
    }

    public NoteId() {
        this(UUID.randomUUID());
    }
}
