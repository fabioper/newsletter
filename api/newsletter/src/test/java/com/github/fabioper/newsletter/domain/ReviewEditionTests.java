package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.review.Review;
import com.github.fabioper.newsletter.domain.review.ReviewStatus;
import com.github.fabioper.newsletter.domain.review.events.ReviewApprovedEvent;
import com.github.fabioper.newsletter.domain.review.events.ReviewRejectedEvent;
import com.github.fabioper.newsletter.domain.review.events.ReviewStartedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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
        var reviewerId = UUID.randomUUID();
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));
        edition.addNote("Teste 1", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.closeEdition();
        edition.submitToReview();

        var review = new Review(edition, reviewerId);

        assertEquals(ReviewStatus.IN_PROGRESS, review.getStatus());
        assertEquals(edition, review.getEdition());
        assertEquals(reviewerId, review.getReviewerId());
    }

    @Test
    @DisplayName("should raise event when starting review")
    void shouldRaiseEventWhenStartingReview() {
        var reviewerId = UUID.randomUUID();
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));
        edition.addNote("Teste 1", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.closeEdition();
        edition.submitToReview();

        var review = new Review(edition, reviewerId);

        assertThat(review.getDomainEvents(), hasItems(
            new ReviewStartedEvent(review.getId())
        ));
    }

    @Test
    @DisplayName("should not start review if edition is not closed")
    void shouldNotStartIfEditionIsNotClosed() {
        var reviewerId = UUID.randomUUID();
        var edition = new Edition("Test", UUID.randomUUID(), new Category("Category"));

        edition.addNote("Note", shortContent, UUID.randomUUID(), new Editorial("Editorial"));

        assertThrows(IllegalStateException.class, () -> new Review(edition, reviewerId));
    }

    @Test
    @DisplayName("should update status and raise event when approving review")
    void shouldUpdateStatusAndRaiseEventWhenApproving() {
        var reviewerId = UUID.randomUUID();
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));
        edition.addNote("Teste 1", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.closeEdition();
        edition.submitToReview();

        var review = new Review(edition, reviewerId);

        review.approve();

        assertEquals(ReviewStatus.APPROVED, review.getStatus());

        assertThat(review.getDomainEvents(), hasItems(
            new ReviewApprovedEvent(review.getId())
        ));
    }

    @Test
    @DisplayName("should update status and comment and raise event when rejecting review")
    void shouldUpdateStatusAndRaiseEventWhenRejecting() {
        var reviewerId = UUID.randomUUID();
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));
        edition.addNote("Teste 1", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.closeEdition();
        edition.submitToReview();

        var review = new Review(edition, reviewerId);

        var comment = "The reason is...";
        review.reject(comment);

        assertEquals(ReviewStatus.REJECTED, review.getStatus());
        assertEquals(comment, review.getComment());

        assertThat(review.getDomainEvents(), hasItems(
            new ReviewRejectedEvent(review.getId(), comment)
        ));
    }

    @Test
    @DisplayName("should not be able to reject review without comment")
    void shouldNotBeAbleToRejectReviewWithoutComment() {
        var reviewerId = UUID.randomUUID();
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));
        edition.addNote("Teste 1", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.closeEdition();
        edition.submitToReview();

        var review = new Review(edition, reviewerId);

        assertThrows(IllegalArgumentException.class, () -> review.reject(null));
        assertThrows(IllegalArgumentException.class, () -> review.reject(""));

        assertEquals(ReviewStatus.IN_PROGRESS, review.getStatus());

        assertThat(review.getDomainEvents(), not(hasItems(
            new ReviewRejectedEvent(review.getId(), review.getComment())
        )));
    }

    @Test
    @DisplayName("should not be able to reject review that is not in progress")
    void shouldNotBeAbleToRejectReviewThatIsNotInProgress() {
        var reviewerId = UUID.randomUUID();
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));
        edition.addNote("Teste 1", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.closeEdition();
        edition.submitToReview();

        var review = new Review(edition, reviewerId);

        review.approve();

        assertThrows(IllegalStateException.class, () -> review.reject("Reason is..."));

        assertEquals(ReviewStatus.APPROVED, review.getStatus());
    }

    @Test
    @DisplayName("should not be able to approve review that is not in progress")
    void shouldNotBeAbleToApproveReviewThatIsNotInProgress() {
        var reviewerId = UUID.randomUUID();
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));
        edition.addNote("Teste 1", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.closeEdition();
        edition.submitToReview();

        var review = new Review(edition, reviewerId);

        review.reject("Reason is...");

        assertThrows(IllegalStateException.class, review::approve);
        assertEquals(ReviewStatus.REJECTED, review.getStatus());
    }
}
