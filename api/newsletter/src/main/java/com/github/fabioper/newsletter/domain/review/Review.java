package com.github.fabioper.newsletter.domain.review;

import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.EditionId;
import com.github.fabioper.newsletter.domain.shared.AggregateRoot;
import com.github.fabioper.newsletter.domain.shared.Guard;
import jakarta.persistence.*;

@Entity
public class Review extends AggregateRoot {
    @EmbeddedId
    private ReviewId id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "reviewer_id", nullable = false))
    private ReviewerId reviewerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ReviewStatus status;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "edition_id", nullable = false))
    private EditionId editionId;

    private String comment;

    public Review() {
    }

    private Review(EditionId editionId, ReviewerId reviewerId) {
        this.id = new ReviewId();
        this.editionId = editionId;
        this.reviewerId = reviewerId;
        this.status = ReviewStatus.IN_PROGRESS;

        this.raiseEvent(new ReviewStartedEvent(this.getId()));
    }

    public static Review startReviewOf(Edition edition, ReviewerId reviewerId) {
        Guard.againstNullOrEmpty(edition, "Edition cannot be null");
        Guard.againstNullOrEmpty(reviewerId, "ReviewerId cannot be null");

        if (!edition.isAvailableForReview()) {
            throw new IllegalStateException("Edition is not closed");
        }

        return new Review(edition.getId(), reviewerId);
    }

    //region Getters
    public ReviewId getId() {
        return id;
    }

    public ReviewerId getReviewerId() {
        return reviewerId;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public EditionId getEditionId() {
        return editionId;
    }

    public String getComment() {
        return comment;
    }
    //endregion

    public void approve() {
        ensureIsInProgress();

        this.status = ReviewStatus.APPROVED;
        this.raiseEvent(new ReviewApprovedEvent(this.getId()));
    }

    public void deny(String comment) {
        Guard.againstNullOrEmpty(comment, "Comment cannot be empty");
        ensureIsInProgress();

        this.status = ReviewStatus.DENIED;
        this.comment = comment;
        this.raiseEvent(new ReviewDeniedEvent(this.getId()));
    }

    private void ensureIsInProgress() {
        if (!status.isInProgress()) {
            throw new IllegalStateException("Review is not in progress");
        }
    }
}
