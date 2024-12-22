package com.github.fabioper.newsletter.domain.review;

import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.review.events.ReviewApprovedEvent;
import com.github.fabioper.newsletter.domain.review.events.ReviewRejectedEvent;
import com.github.fabioper.newsletter.domain.review.events.ReviewStartedEvent;
import com.github.fabioper.newsletter.domain.shared.Guard;
import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

import java.util.Objects;
import java.util.UUID;

public class Review extends BaseEntity {
    private final UUID id;
    private final UUID reviewerId;
    private final UUID editionId;
    private ReviewStatus status;
    private String comment;

    public Review(Edition edition, UUID reviewerId) {
        ensureEditionIsAvailableForReview(edition);

        this.id = UUID.randomUUID();
        this.editionId = edition.getId();
        this.reviewerId = reviewerId;
        this.status = ReviewStatus.IN_PROGRESS;
        this.comment = "";

        raiseEvent(new ReviewStartedEvent(this.id));
    }

    //region Getters
    public UUID getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public UUID getReviewerId() {
        return reviewerId;
    }

    public UUID getEditionId() {
        return editionId;
    }

    public ReviewStatus getStatus() {
        return status;
    }
    //endregion

    public void approve() {
        ensureReviewIsInProgress(this);

        this.status = ReviewStatus.APPROVED;

        raiseEvent(new ReviewApprovedEvent(this.id));
    }

    public void reject(String comment) {
        Guard.againstNullOrEmpty(comment, "Cannot reject review without a comment");
        ensureReviewIsInProgress(this);

        this.status = ReviewStatus.REJECTED;
        this.comment = comment;

        raiseEvent(new ReviewRejectedEvent(this.id, comment));
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

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Review review = (Review) object;
        return Objects.equals(getId(), review.getId());
    }
}
