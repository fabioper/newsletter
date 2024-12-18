package com.github.fabioper.newsletter.domain.editorial;

import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

public class Editorial extends BaseEntity {
    private final EditorialId id;
    private final String name;

    public Editorial(String name) {
        this.id = new EditorialId();
        this.name = name;
    }

    public EditorialId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
