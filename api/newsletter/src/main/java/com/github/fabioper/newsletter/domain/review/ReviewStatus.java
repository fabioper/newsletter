package com.github.fabioper.newsletter.domain.review;

public enum ReviewStatus {
    IN_PROGRESS,
    APPROVED,
    DENIED;

    public boolean isInProgress() {
        return this == IN_PROGRESS;
    }

    public boolean isApproved() {
        return this == APPROVED;
    }

    public boolean isDenied() {
        return this == DENIED;
    }
}
