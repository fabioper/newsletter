package com.github.fabioper.newsletter.domain.collaborator;

import java.util.UUID;

public class Collaborator {
    private final UUID id;

    public Collaborator(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
