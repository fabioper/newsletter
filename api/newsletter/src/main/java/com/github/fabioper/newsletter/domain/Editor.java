package com.github.fabioper.newsletter.domain;

import java.util.UUID;

public class Editor {
    private final UUID id;

    public Editor() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public Edition createEdition(Category category) {
        return new Edition(this, category);
    }
}
