package com.github.fabioper.newsletter.domain.editorial;

import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.Objects;
import java.util.UUID;

public class Editorial extends BaseEntity {
    private final UUID id;
    private final String name;

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

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Editorial editorial = (Editorial) object;
        return Objects.equals(getId(), editorial.getId());
    }
}
