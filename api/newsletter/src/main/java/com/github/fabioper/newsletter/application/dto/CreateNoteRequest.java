package com.github.fabioper.newsletter.application.dto;

public record CreateNoteRequest(
    String title,
    String content
) {
}
