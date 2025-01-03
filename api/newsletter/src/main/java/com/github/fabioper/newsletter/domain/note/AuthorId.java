package com.github.fabioper.newsletter.domain.note;

import com.github.fabioper.newsletter.domain.shared.EntityId;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class AuthorId extends EntityId {
    protected AuthorId(UUID value) {
        super(value);
    }

    public AuthorId() {
        super(UUID.randomUUID());
    }

    public AuthorId(String value) {
        super(UUID.fromString(value));
    }
}
