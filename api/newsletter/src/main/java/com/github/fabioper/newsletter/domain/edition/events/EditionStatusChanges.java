package com.github.fabioper.newsletter.domain.edition.events;

import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletterapi.abstractions.DomainEvent;

import java.util.UUID;

public record EditionStatusChanges(
    UUID editionId,
    Status from,
    Status to
) implements DomainEvent {
}
