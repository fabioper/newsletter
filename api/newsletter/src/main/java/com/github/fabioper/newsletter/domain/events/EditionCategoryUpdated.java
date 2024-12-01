package com.github.fabioper.newsletter.domain.events;

import com.github.fabioper.newsletterapi.abstractions.DomainEvent;

import java.util.UUID;

public record EditionCategoryUpdated(
    UUID editionId,
    UUID oldCategoryId,
    UUID newCategoryId
) implements DomainEvent {
}
