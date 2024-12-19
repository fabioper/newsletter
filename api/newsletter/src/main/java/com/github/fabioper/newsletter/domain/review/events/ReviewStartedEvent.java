package com.github.fabioper.newsletter.domain.review.events;

import com.github.fabioper.newsletterapi.abstractions.DomainEvent;

import java.util.UUID;

public record ReviewStartedEvent(
    UUID reviewId
) implements DomainEvent {
}
