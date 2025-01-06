package com.github.fabioper.newsletter.domain.note;

import com.github.fabioper.newsletter.domain.shared.EntityId;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class NoteId extends EntityId {
    public NoteId() {
        super(UUID.randomUUID());
    }

    public NoteId(String value) {
        super(UUID.fromString(value));
    }

    public NoteId(UUID uuid) {
        super(uuid);
    }
}
