package com.github.fabioper.newsletter.domain.shared;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MappedSuperclass
public abstract class AggregateRoot {
    @Transient
    protected final List<DomainEvent> events = new ArrayList<>();

    protected void raiseEvent(DomainEvent domainEvent) {
        this.events.add(domainEvent);
    }

    public List<DomainEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
