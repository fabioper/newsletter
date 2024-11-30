package com.github.fabioper.newsletter.domain.events;

import com.github.fabioper.newsletter.shared.DomainEvent;

import java.util.UUID;

public record NoteEditorialUpdatedEvent(
    UUID noteId,
    UUID oldEditorialId,
    UUID newEditorialId
) implements DomainEvent {
}
