package com.github.fabioper.newsletter.domain.shared;

import jakarta.persistence.MappedSuperclass;

import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class EntityId {
    private UUID id;

    public EntityId() {
    }

    public EntityId(UUID value) {
        this.id = value;
    }

    public UUID getValue() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        var entityId = (EntityId) object;
        return Objects.equals(getValue(), entityId.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
