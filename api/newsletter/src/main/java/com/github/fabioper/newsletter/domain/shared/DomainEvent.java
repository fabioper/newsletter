package com.github.fabioper.newsletter.domain.shared;

import java.time.Instant;

public abstract class DomainEvent {
    private final Instant occurredAt = Instant.now();

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
