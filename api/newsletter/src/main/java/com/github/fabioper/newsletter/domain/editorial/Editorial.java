package com.github.fabioper.newsletter.domain.editorial;

import com.github.fabioper.newsletterapi.abstractions.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "editorials")
public class Editorial extends AggregateRoot {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    public Editorial() {
    }

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
