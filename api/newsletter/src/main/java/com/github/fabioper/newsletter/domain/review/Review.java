package com.github.fabioper.newsletter.domain.review;

import com.github.fabioper.newsletter.domain.common.Guard;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.review.events.ReviewApprovedEvent;
import com.github.fabioper.newsletter.domain.review.events.ReviewRejectedEvent;
import com.github.fabioper.newsletter.domain.review.events.ReviewStartedEvent;
import com.github.fabioper.newsletter.domain.reviewer.ReviewerId;
import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

public class Review extends BaseEntity {
    private final ReviewId id;
    private final ReviewerId reviewerId;
    private final Edition edition;
    private ReviewStatus status;
    private String comment;

    private Review(Edition edition, ReviewerId reviewerId) {
        this.id = new ReviewId();
        this.edition = edition;
        this.reviewerId = reviewerId;
        this.status = ReviewStatus.IN_PROGRESS;
        this.comment = "";

        raiseEvent(new ReviewStartedEvent(this.id.value()));
    }

    //region Getters
    public ReviewId getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public ReviewerId getReviewerId() {
        return reviewerId;
    }

    public Edition getEdition() {
        return edition;
    }

    public ReviewStatus getStatus() {
        return status;
    }
    //endregion

    public void approve() {
        ensureReviewIsInProgress(this);

        this.status = ReviewStatus.APPROVED;

        raiseEvent(new ReviewApprovedEvent(this.id.value()));
    }

    public void reject(String comment) {
        Guard.againstNullOrEmpty(comment, "Cannot reject review without a comment");
        ensureReviewIsInProgress(this);

        this.status = ReviewStatus.REJECTED;
        this.comment = comment;

        raiseEvent(new ReviewRejectedEvent(this.id.value(), comment));
    }

    public static Review startReviewOf(Edition edition, ReviewerId reviewerId) {
        ensureEditionIsAvailableForReview(edition);
        return new Review(edition, reviewerId);
    }

    private static void ensureReviewIsInProgress(Review review) {
        if (review.status != ReviewStatus.IN_PROGRESS) {
            throw new IllegalStateException("Review is not in progress");
        }
    }

    private static void ensureEditionIsAvailableForReview(Edition edition) {
        if (edition.getStatus() != Status.AVAILABLE_FOR_REVIEW) {
            throw new IllegalStateException("Edition is not available for review");
        }
    }
}
