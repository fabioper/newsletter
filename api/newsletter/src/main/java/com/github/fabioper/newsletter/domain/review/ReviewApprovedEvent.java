package com.github.fabioper.newsletter.domain.review;

import com.github.fabioper.newsletter.domain.shared.DomainEvent;

import java.util.Objects;

public class ReviewApprovedEvent extends DomainEvent {
    private final ReviewId reviewId;

    public ReviewApprovedEvent(ReviewId reviewId) {
        this.reviewId = reviewId;
    }

    public ReviewId getReviewId() {
        return reviewId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ReviewApprovedEvent that = (ReviewApprovedEvent) object;

        return Objects.equals(getReviewId(), that.getReviewId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getReviewId());
    }
}
