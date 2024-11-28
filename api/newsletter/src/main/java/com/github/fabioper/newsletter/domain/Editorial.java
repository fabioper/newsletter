package com.github.fabioper.newsletter.domain;

import java.util.UUID;

public class Editorial {
    private final UUID id;
    private String name;

    public Editorial(String name) {
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
