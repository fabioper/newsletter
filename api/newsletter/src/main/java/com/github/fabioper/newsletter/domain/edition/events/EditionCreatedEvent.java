package com.github.fabioper.newsletter.domain.edition.events;

import com.github.fabioper.newsletterapi.abstractions.DomainEvent;

import java.util.UUID;

public record EditionCreatedEvent(
    UUID editionId
) implements DomainEvent {
}
