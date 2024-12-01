package com.github.fabioper.newsletter.domain.events;

import com.github.fabioper.newsletterapi.abstractions.DomainEvent;

import java.util.UUID;

public record EditionTitleUpdated(
    UUID editionId,
    String oldTitle,
    String newTitle
) implements DomainEvent {
}
