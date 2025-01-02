package com.github.fabioper.newsletter.domain.review;

import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.EditorId;
import com.github.fabioper.newsletter.domain.note.AuthorId;
import com.github.fabioper.newsletter.domain.note.Note;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {
    @Test
    void shouldStartReviewWithInProgressStatus() {
        var edition = editionAvailableForReview();

        var reviewerId = new ReviewerId();
        var review = Review.startReviewOf(edition, reviewerId);

        assertEquals(edition.getId(), review.getEditionId());
        assertEquals(ReviewStatus.IN_PROGRESS, review.getStatus());
        assertEquals(reviewerId, review.getReviewerId());
        assertNotNull(review.getId());
    }

    @Test
    void shouldNotStartReviewIfEditionIsNotAvailableForReview() {
        var edition = new Edition("Title", new EditorId());
        assertThrows(IllegalStateException.class, () -> Review.startReviewOf(edition, new ReviewerId()));

        var note = new Note("Title", "Content", new AuthorId());
        edition.assign(note);
        note.close();
        edition.close();

        assertThrows(IllegalStateException.class, () -> Review.startReviewOf(edition, new ReviewerId()));

        edition.submitToReview();
        assertDoesNotThrow(() -> Review.startReviewOf(edition, new ReviewerId()));
    }

    @Test
    void shouldNotStartReviewIfReviewerIdIsNotValid() {
        var edition = new Edition("Title", new EditorId());
        assertThrows(IllegalArgumentException.class, () -> Review.startReviewOf(edition, null));
    }

    @Test
    void shouldNotStartReviewIfEditionIsNotValid() {
        assertThrows(IllegalArgumentException.class, () -> Review.startReviewOf(null, new ReviewerId()));
    }

    @Test
    void shouldApproveEdition() {
        var edition = editionAvailableForReview();

        var review = Review.startReviewOf(edition, new ReviewerId());
        review.approve();

        assertEquals(ReviewStatus.APPROVED, review.getStatus());
    }

    @Test
    void shouldNotApproveReviewIfReviewIsNotInProgress() {
        var edition = editionAvailableForReview();

        var review = Review.startReviewOf(edition, new ReviewerId());
        review.approve();

        assertThrows(IllegalStateException.class, review::approve);
    }

    @Test
    void shouldDenyReviewWithAValidComment() {
        var edition = editionAvailableForReview();

        var review = Review.startReviewOf(edition, new ReviewerId());
        review.deny("This is a comment...");

        assertEquals(ReviewStatus.DENIED, review.getStatus());
        assertEquals("This is a comment...", review.getComment());
    }

    @Test
    void shouldNotDenyReviewWithoutAValidComment() {
        var edition = editionAvailableForReview();

        var review = Review.startReviewOf(edition, new ReviewerId());

        assertThrows(IllegalArgumentException.class, () -> review.deny(null));
        assertThrows(IllegalArgumentException.class, () -> review.deny(""));
        assertThrows(IllegalArgumentException.class, () -> review.deny("   "));

        assertEquals(ReviewStatus.IN_PROGRESS, review.getStatus());
    }

    private static Edition editionAvailableForReview() {
        var edition = new Edition("Title", new EditorId());

        var note = new Note("Title", "Content", new AuthorId());
        edition.assign(note);
        note.close();
        edition.close();
        edition.submitToReview();

        return edition;
    }
}
