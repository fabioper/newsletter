package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.shared.EntityId;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class EditionId extends EntityId {
    protected EditionId() {
        super(UUID.randomUUID());
    }

    public EditionId(UUID value) {
        super(value);
    }

    public EditionId(String value) {
        super(UUID.fromString(value));
    }
}
