package com.github.fabioper.newsletterapi.abstractions;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public abstract class AggregateRoot {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public List<DomainEvent> getDomainEvents() {
        return unmodifiableList(domainEvents);
    }

    protected void raiseEvent(DomainEvent domainEvent) {
        domainEvents.add(domainEvent);
    }
}
