package com.github.fabioper.newsletter.domain.events;

import com.github.fabioper.newsletter.shared.DomainEvent;

import java.util.UUID;

public record NoteTitleUpdatedEvent(
    UUID noteId,
    String oldTitle,
    String newTitle
) implements DomainEvent {
}
