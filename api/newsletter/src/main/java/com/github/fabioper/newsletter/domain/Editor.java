package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.UUID;

public class Editor extends BaseEntity {
    private final UUID id;

    public Editor() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public Edition createEdition(String title, Category category) {
        return new Edition(title, this, category);
    }
}
