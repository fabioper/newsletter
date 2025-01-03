package com.github.fabioper.newsletter.domain.review;

import com.github.fabioper.newsletter.domain.shared.EntityId;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class ReviewId extends EntityId {
    protected ReviewId(UUID value) {
        super(value);
    }

    public ReviewId() {
        super(UUID.randomUUID());
    }

    public ReviewId(String value) {
        super(UUID.fromString(value));
    }
}
