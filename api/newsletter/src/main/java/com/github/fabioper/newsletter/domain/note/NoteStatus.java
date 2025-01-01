package com.github.fabioper.newsletter.domain.note;

public enum NoteStatus {
    OPEN,
    CLOSED,
    PUBLISHED;

    public boolean isOpen() {
        return this == OPEN;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }

    public boolean isPublished() {
        return this == PUBLISHED;
    }
}
