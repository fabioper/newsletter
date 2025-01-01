package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.shared.EntityId;

import java.util.UUID;

public class EditionId extends EntityId<UUID> {
    public EditionId() {
        super(UUID.randomUUID());
    }
}
