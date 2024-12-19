package com.github.fabioper.newsletter.domain.edition;

public enum Status {
    DRAFT,
    CLOSED,
    AVAILABLE_FOR_REVIEW,
    UNDER_REVIEW,
    PENDING_ADJUSTMENTS,
    APPROVED,
    PUBLISHED;

    public boolean isUnderReview() {
        return this == Status.UNDER_REVIEW;
    }

    public boolean isDraft() {
        return this == Status.DRAFT;
    }

    public boolean isPendingAdjustments() {
        return this == Status.PENDING_ADJUSTMENTS;
    }

    public boolean isAvailableForReview() {
        return this == Status.AVAILABLE_FOR_REVIEW;
    }

    public boolean isClosed() {
        return this == Status.CLOSED;
    }
}
