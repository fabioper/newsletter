package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.AuthorId;
import com.github.fabioper.newsletter.domain.category.CategoryId;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.editor.EditorId;
import com.github.fabioper.newsletter.domain.editorial.EditorialId;
import com.github.fabioper.newsletter.domain.review.Review;
import com.github.fabioper.newsletter.domain.review.ReviewStatus;
import com.github.fabioper.newsletter.domain.reviewer.ReviewerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When starting review")
public class StartReviewTests {
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
    @DisplayName("should not start if edition is not closed")
    void shouldNotStartIfEditionIsNotClosed() {
        var reviewerId = new ReviewerId();
        var edition = new Edition("Test", new EditorId(), new CategoryId());

        edition.addNote("Note", shortContent, new AuthorId(), new EditorialId());

        assertThrows(IllegalStateException.class, () -> Review.startReviewOf(edition, reviewerId));
    }
}
