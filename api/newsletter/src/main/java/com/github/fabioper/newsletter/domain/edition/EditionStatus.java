package com.github.fabioper.newsletter.domain.edition;

public enum EditionStatus {
    OPEN,
    CLOSED,
    AVAILABLE_FOR_REVIEW,
    UNDER_REVIEW,
    AVAILABLE_FOR_PUBLICATION;

    public boolean isOpen() {
        return this == OPEN;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }

    public boolean isAvailableForReview() {
        return this == AVAILABLE_FOR_REVIEW;
    }

    public boolean isAvailableForPublication() {
        return this == AVAILABLE_FOR_PUBLICATION;
    }

    public boolean isUnderReview() {
        return this == UNDER_REVIEW;
    }
}
