package com.github.fabioper.newsletter.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseEntity {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    protected void raiseDomainEvent(DomainEvent domainEvent) {
        domainEvents.add(domainEvent);
    }

    public void clearEvents() {
        domainEvents.clear();
    }
}
