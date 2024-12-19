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

@DisplayName("When approving edition")
public class ApproveEditionTests {
    @Test
    @DisplayName("should update status to approved")
    void shouldUpdateStatusToApproved() {
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Note1", shortContent, new AuthorId(), new EditorialId());

        edition.closeEdition();
        edition.submitToReview();
        edition.putUnderReview();
        edition.approve();

        assertEquals(Status.APPROVED, edition.getStatus());
    }

    @Test
    @DisplayName("should not approve edition if it is not under review")
    void shouldNotApproveIfItIsNotUnderReview() {
        var edition = new Edition("Title", new EditorId(), new CategoryId());
        edition.addNote("Note1", shortContent, new AuthorId(), new EditorialId());

        edition.closeEdition();
        edition.submitToReview();

        var originalStatus = edition.getStatus();
        assertThrows(IllegalStateException.class, edition::approve);
        assertEquals(originalStatus, edition.getStatus());
    }
}
