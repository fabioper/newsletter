package com.github.fabioper.newsletter.domain.review;

import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.EditionId;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.reviewer.ReviewerId;
import com.github.fabioper.newsletterapi.abstractions.BaseEntity;

public class Review extends BaseEntity {
    private final ReviewId id;
    private final ReviewerId reviewerId;
    private final EditionId editionId;
    private final ReviewStatus status;
    private final String comment;

    private Review(EditionId editionId, ReviewerId reviewerId) {
        this.id = new ReviewId();
        this.editionId = editionId;
        this.reviewerId = reviewerId;
        this.status = ReviewStatus.IN_PROGRESS;
        this.comment = "";
    }

    public ReviewId getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public ReviewerId getReviewerId() {
        return reviewerId;
    }

    public EditionId getEditionId() {
        return editionId;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public static Review startReviewOf(Edition edition, ReviewerId reviewerId) {
        if (edition.getStatus() != Status.AVAILABLE_FOR_REVIEW) {
            throw new IllegalStateException("Edition is not available for review");
        }

        return new Review(edition.getId(), reviewerId);
    }
}
