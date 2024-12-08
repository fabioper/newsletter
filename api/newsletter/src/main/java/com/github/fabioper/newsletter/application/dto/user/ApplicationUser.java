package com.github.fabioper.newsletter.application.dto.user;

import java.util.List;
import java.util.UUID;

public record ApplicationUser(UUID id, List<String> roles) {
    private boolean hasRole(String role) {
        return roles().contains(role);
    }

    public boolean isAuthor() {
        return hasRole("author");
    }

    public boolean isEditor() {
        return hasRole("editor");
    }
}
