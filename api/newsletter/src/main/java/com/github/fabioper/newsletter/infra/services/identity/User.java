package com.github.fabioper.newsletter.infra.services.identity;

import java.util.List;
import java.util.UUID;

public record User(UUID id, List<String> roles) {
    public boolean hasRole(String role) {
        return roles().contains(role);
    }
}
