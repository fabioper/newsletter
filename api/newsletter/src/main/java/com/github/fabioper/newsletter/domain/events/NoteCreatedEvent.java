package com.github.fabioper.newsletter.domain.events;

import com.github.fabioper.newsletterapi.abstractions.DomainEvent;

import java.util.UUID;

public record NoteCreatedEvent(
    UUID noteId
) implements DomainEvent {
}
