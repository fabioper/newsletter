package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.AuthorId;
import com.github.fabioper.newsletter.domain.category.CategoryId;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.editor.EditorId;
import com.github.fabioper.newsletter.domain.editorial.EditorialId;
import com.github.fabioper.newsletter.domain.review.Review;
import com.github.fabioper.newsletter.domain.review.ReviewStatus;
import com.github.fabioper.newsletter.domain.review.events.ReviewApprovedEvent;
import com.github.fabioper.newsletter.domain.review.events.ReviewRejectedEvent;
import com.github.fabioper.newsletter.domain.review.events.ReviewStartedEvent;
import com.github.fabioper.newsletter.domain.reviewer.ReviewerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When reviewing edition")
public class ReviewEditionTests {
    @Test
    @DisplayName("should start review with in progress status")
    void shouldStartWithInProgressStatus() {
        var reviewerId = new ReviewerId();
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Teste 1", longContent, new AuthorId(), new EditorialId());
        edition.closeEdition();
        edition.submitToReview();

        var review = Review.startReviewOf(edition, reviewerId);

        assertEquals(ReviewStatus.IN_PROGRESS, review.getStatus());
        assertEquals(edition.getId(), review.getEditionId());
        assertEquals(reviewerId, review.getReviewerId());
    }

    @Test
    @DisplayName("should raise event when starting review")
    void shouldRaiseEventWhenStartingReview() {
        var reviewerId = new ReviewerId();
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Teste 1", longContent, new AuthorId(), new EditorialId());
        edition.closeEdition();
        edition.submitToReview();

        var review = Review.startReviewOf(edition, reviewerId);

        assertThat(review.getDomainEvents(), hasItems(
            new ReviewStartedEvent(review.getId().value())
        ));
    }

    @Test
    @DisplayName("should not start review if edition is not closed")
    void shouldNotStartIfEditionIsNotClosed() {
        var reviewerId = new ReviewerId();
        var edition = new Edition("Test", new EditorId(), new CategoryId());

        edition.addNote("Note", shortContent, new AuthorId(), new EditorialId());

        assertThrows(IllegalStateException.class, () -> Review.startReviewOf(edition, reviewerId));
    }

    @Test
    @DisplayName("should update status and raise event when approving review")
    void shouldUpdateStatusAndRaiseEventWhenApproving() {
        var reviewerId = new ReviewerId();
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Teste 1", longContent, new AuthorId(), new EditorialId());
        edition.closeEdition();
        edition.submitToReview();

        var review = Review.startReviewOf(edition, reviewerId);

        review.approve();

        assertEquals(ReviewStatus.APPROVED, review.getStatus());

        assertThat(review.getDomainEvents(), hasItems(
            new ReviewApprovedEvent(review.getId().value())
        ));
    }

    @Test
    @DisplayName("should update status and comment and raise event when rejecting review")
    void shouldUpdateStatusAndRaiseEventWhenRejecting() {
        var reviewerId = new ReviewerId();
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Teste 1", longContent, new AuthorId(), new EditorialId());
        edition.closeEdition();
        edition.submitToReview();

        var review = Review.startReviewOf(edition, reviewerId);

        var comment = "The reason is...";
        review.reject(comment);

        assertEquals(ReviewStatus.REJECTED, review.getStatus());
        assertEquals(comment, review.getComment());

        assertThat(review.getDomainEvents(), hasItems(
            new ReviewRejectedEvent(review.getId().value(), comment)
        ));
    }

    @Test
    @DisplayName("should not be able to reject review without comment")
    void shouldNotBeAbleToRejectReviewWithoutComment() {
        var reviewerId = new ReviewerId();
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Teste 1", longContent, new AuthorId(), new EditorialId());
        edition.closeEdition();
        edition.submitToReview();

        var review = Review.startReviewOf(edition, reviewerId);

        assertThrows(IllegalArgumentException.class, () -> review.reject(null));
        assertThrows(IllegalArgumentException.class, () -> review.reject(""));

        assertEquals(ReviewStatus.IN_PROGRESS, review.getStatus());

        assertThat(review.getDomainEvents(), not(hasItems(
            new ReviewRejectedEvent(review.getId().value(), review.getComment())
        )));
    }

    @Test
    @DisplayName("should not be able to reject review that is not in progress")
    void shouldNotBeAbleToRejectReviewThatIsNotInProgress() {
        var reviewerId = new ReviewerId();
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Teste 1", longContent, new AuthorId(), new EditorialId());
        edition.closeEdition();
        edition.submitToReview();

        var review = Review.startReviewOf(edition, reviewerId);

        review.approve();

        assertThrows(IllegalStateException.class, () -> review.reject("Reason is..."));

        assertEquals(ReviewStatus.APPROVED, review.getStatus());
    }

    @Test
    @DisplayName("should not be able to approve review that is not in progress")
    void shouldNotBeAbleToApproveReviewThatIsNotInProgress() {
        var reviewerId = new ReviewerId();
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Teste 1", longContent, new AuthorId(), new EditorialId());
        edition.closeEdition();
        edition.submitToReview();

        var review = Review.startReviewOf(edition, reviewerId);

        review.reject("Reason is...");

        assertThrows(IllegalStateException.class, review::approve);
        assertEquals(ReviewStatus.REJECTED, review.getStatus());
    }
}
