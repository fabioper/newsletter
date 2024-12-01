package com.github.fabioper.newsletter.domain.editorial;

import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.UUID;

public class Editorial extends BaseEntity {
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