package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.shared.EntityId;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class EditorId extends EntityId {
    public EditorId() {
        super(UUID.randomUUID());
    }

    public EditorId(String userId) {
        super(UUID.fromString(userId));
    }
}
