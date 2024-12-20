package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.AuthorId;
import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.edition.events.EditionStatusChanges;
import com.github.fabioper.newsletter.domain.editor.EditorId;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When submitting edition to review")
public class SubmitEditionToReviewTests {
    @Test
    @DisplayName("should update edition status")
    void shouldUpdateEditionStatus() {
        var edition = new Edition("Title", new EditorId(), new Category("Category"));

        edition.addNote("Note 1", shortContent, new AuthorId(), new Editorial("Editorial"));

        edition.closeEdition();
        edition.submitToReview();

        assertEquals(Status.AVAILABLE_FOR_REVIEW, edition.getStatus());
        assertThat(edition.getDomainEvents(), hasItems(
            new EditionStatusChanges(edition.getId().value(), Status.CLOSED, Status.AVAILABLE_FOR_REVIEW)
        ));
    }

    @Test
    @DisplayName("should not update edition if status is different than closed")
    void shouldNotUpdateEditionIfStatusIsNotClosed() {
        var edition = new Edition("Title", new EditorId(), new Category("Category"));

        edition.addNote("Note 1", shortContent, new AuthorId(), new Editorial("Editorial"));

        var originalStatus = edition.getStatus();

        assertThrows(IllegalStateException.class, edition::submitToReview);

        assertEquals(originalStatus, edition.getStatus());
    }
}
