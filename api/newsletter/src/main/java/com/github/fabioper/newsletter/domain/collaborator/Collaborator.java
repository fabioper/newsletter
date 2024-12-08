package com.github.fabioper.newsletter.domain.collaborator;

import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.UUID;

public class Collaborator extends BaseEntity {
    private final UUID id;

    public Collaborator(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
