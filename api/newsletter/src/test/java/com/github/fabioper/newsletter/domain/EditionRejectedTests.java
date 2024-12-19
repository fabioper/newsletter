package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.AuthorId;
import com.github.fabioper.newsletter.domain.category.CategoryId;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.editor.EditorId;
import com.github.fabioper.newsletter.domain.editorial.EditorialId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When an edition review is rejected")
public class EditionRejectedTests {
    @Test
    @DisplayName("should put edition in pending adjustments status")
    void shouldUpdateStatusToPendingAdjustments() {
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Note1", shortContent, new AuthorId(), new EditorialId());

        edition.closeEdition();
        edition.submitToReview();
        edition.putUnderReview();
        edition.putAsPendingAdjustments();

        assertEquals(Status.PENDING_ADJUSTMENTS, edition.getStatus());
    }

    @Test
    @DisplayName("should not change status if it is not under review")
    void shouldNotRejectIfItIsNotUnderReview() {
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Note1", shortContent, new AuthorId(), new EditorialId());

        edition.closeEdition();
        edition.submitToReview();

        var originalStatus = edition.getStatus();
        assertThrows(IllegalStateException.class, edition::putAsPendingAdjustments);
        assertEquals(originalStatus, edition.getStatus());
    }
}
