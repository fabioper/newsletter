package com.github.fabioper.newsletter.domain.review;

import com.github.fabioper.newsletter.domain.shared.EntityId;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class ReviewerId extends EntityId {
    protected ReviewerId(UUID value) {
        super(value);
    }

    public ReviewerId() {
        super(UUID.randomUUID());
    }

    public ReviewerId(String value) {
        super(UUID.fromString(value));
    }
}
