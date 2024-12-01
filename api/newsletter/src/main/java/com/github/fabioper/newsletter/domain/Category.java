package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.UUID;

public class Category extends BaseEntity {
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
