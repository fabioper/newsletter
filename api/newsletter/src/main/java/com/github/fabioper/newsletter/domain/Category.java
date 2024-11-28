package com.github.fabioper.newsletter.domain;

import java.util.UUID;

public class Category {
    private final UUID id;
    private String name;

    public Category(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
