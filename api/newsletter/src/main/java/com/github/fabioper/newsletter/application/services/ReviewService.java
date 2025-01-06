package com.github.fabioper.newsletter.application.services;

import com.github.fabioper.newsletter.application.dto.request.DenyReviewRequest;
import com.github.fabioper.newsletter.application.dto.request.StartReviewRequest;
import com.github.fabioper.newsletter.domain.edition.EditionId;
import com.github.fabioper.newsletter.domain.edition.EditionRepository;
import com.github.fabioper.newsletter.domain.review.Review;
import com.github.fabioper.newsletter.domain.review.ReviewId;
import com.github.fabioper.newsletter.domain.review.ReviewRepository;
import com.github.fabioper.newsletter.domain.review.ReviewerId;
import com.github.fabioper.newsletter.domain.shared.AggregateRoot;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ApplicationEventPublisher eventPublisher;
    private final ReviewRepository reviewRepository;
    private final EditionRepository editionRepository;

    public ReviewService(
        ApplicationEventPublisher eventPublisher,
        ReviewRepository reviewRepository,
        EditionRepository editionRepository
    ) {
        this.eventPublisher = eventPublisher;
        this.reviewRepository = reviewRepository;
        this.editionRepository = editionRepository;
    }

    public void startReview(StartReviewRequest request, String userId) {
        var edition = this.editionRepository.findById(new EditionId(request.editionId()))
            .orElseThrow();

        var review = Review.startReviewOf(edition, new ReviewerId(userId));

        this.reviewRepository.save(review);

        publishEventsOf(review);
    }

    public void approveReview(String reviewId) {
        var review = this.reviewRepository.findById(new ReviewId(reviewId))
            .orElseThrow();

        review.approve();

        this.reviewRepository.save(review);

        publishEventsOf(review);
    }

    public void denyReview(String reviewId, DenyReviewRequest request) {
        var review = this.reviewRepository.findById(new ReviewId(reviewId))
            .orElseThrow();

        review.deny(request.comment());

        this.reviewRepository.save(review);

        publishEventsOf(review);
    }

    private void publishEventsOf(AggregateRoot entity) {
        entity.getEvents().forEach(eventPublisher::publishEvent);
    }
}
