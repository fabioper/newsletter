package com.github.fabioper.newsletter.domain.shared;

import java.util.Objects;

public abstract class EntityId<TId> {
    private final TId value;

    public EntityId(TId value) {
        this.value = value;
    }

    public TId getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
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
