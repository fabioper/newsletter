package com.github.fabioper.newsletter.application.dto;

import java.util.UUID;

public record EditionResponse(
    UUID id,
    String title,
    String status,
    UUID editorId
) {
}
