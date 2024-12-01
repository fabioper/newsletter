package com.github.fabioper.newsletterapi.events;

import com.github.fabioper.newsletterapi.abstractions.IntegrationEvent;

import java.util.UUID;

public record NoteCreatedIntegrationEvent(
    UUID noteId,
    String title,
    String content,
    UUID authorId
) implements IntegrationEvent {
}
