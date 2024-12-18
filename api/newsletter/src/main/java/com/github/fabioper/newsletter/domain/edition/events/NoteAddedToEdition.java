package com.github.fabioper.newsletter.domain.edition.events;

import com.github.fabioper.newsletterapi.abstractions.DomainEvent;

import java.util.UUID;

public record NoteAddedToEdition(
    UUID noteId,
    UUID editionId
) implements DomainEvent {
}
