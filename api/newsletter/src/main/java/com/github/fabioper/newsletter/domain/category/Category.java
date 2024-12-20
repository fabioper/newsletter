package com.github.fabioper.newsletter.domain.category;

import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.Objects;
import java.util.UUID;

public class Category extends BaseEntity {
    private final UUID id;
    private final String name;

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

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Category category = (Category) object;
        return Objects.equals(getId(), category.getId());
    }
}
