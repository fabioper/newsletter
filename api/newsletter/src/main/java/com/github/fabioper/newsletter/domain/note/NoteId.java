package com.github.fabioper.newsletter.domain.note;

import com.github.fabioper.newsletter.domain.shared.EntityId;

import java.util.UUID;

public class NoteId extends EntityId<UUID> {
    public NoteId() {
        super(UUID.randomUUID());
    }
}
