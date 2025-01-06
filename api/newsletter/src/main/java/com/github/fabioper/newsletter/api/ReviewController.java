package com.github.fabioper.newsletter.api;

import com.github.fabioper.newsletter.application.dto.request.DenyReviewRequest;
import com.github.fabioper.newsletter.application.dto.request.StartReviewRequest;
import com.github.fabioper.newsletter.application.services.ReviewService;
import com.github.fabioper.newsletter.infra.security.annotations.IsReviewer;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @IsReviewer
    @PostMapping
    public void startReview(@RequestBody StartReviewRequest request, @AuthenticationPrincipal Jwt principal) {
        this.reviewService.startReview(request, principal.getSubject());
    }

    @IsReviewer
    @PostMapping("{reviewId}/approve")
    public void approveReview(@PathVariable String reviewId) {
        this.reviewService.approveReview(reviewId);
    }

    @IsReviewer
    @PostMapping("{reviewId}/deny")
    public void approveReview(@PathVariable String reviewId, @RequestBody DenyReviewRequest request) {
        this.reviewService.denyReview(reviewId, request);
    }
}
