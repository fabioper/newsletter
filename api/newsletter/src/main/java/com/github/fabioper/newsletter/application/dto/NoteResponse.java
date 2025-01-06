package com.github.fabioper.newsletter.application.dto;

import java.util.UUID;

public record NoteResponse(
    UUID id,
    String title,
    String content,
    String status
) {
}
