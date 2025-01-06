package com.github.fabioper.newsletter.application.dto.request;

import java.util.UUID;

public record AssignNoteRequest(
    UUID noteId
) {
}
