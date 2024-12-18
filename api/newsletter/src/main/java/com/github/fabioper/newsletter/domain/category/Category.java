package com.github.fabioper.newsletter.domain.category;

import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

public class Category extends BaseEntity {
    private final CategoryId id;
    private final String name;

    public Category(String name) {
        this.id = new CategoryId();
        this.name = name;
    }

    public CategoryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
