package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.note.Note;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EditionTest {

    @Test
    void shouldCreateEditionWithOpenStatus() {
        var edition = new Edition("Edition title");
        assertEquals("Edition title", edition.getTitle());
        assertEquals(EditionStatus.OPEN, edition.getStatus());
    }

    @Test
    void shouldNotCreateEditionWithEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Edition(""));
        assertThrows(IllegalArgumentException.class, () -> new Edition(null));
        assertThrows(IllegalArgumentException.class, () -> new Edition("  "));
    }

    @Test
    void shouldUpdateTitle() {
        var edition = new Edition("Edition title");
        edition.updateTitle("Edition title updated");

        assertEquals("Edition title updated", edition.getTitle());
    }

    @Test
    void shouldNotUpdateTitleWithEmptyTitle() {
        var edition = new Edition("Edition title");

        assertThrows(IllegalArgumentException.class, () -> edition.updateTitle(""));
        assertThrows(IllegalArgumentException.class, () -> edition.updateTitle(null));
        assertThrows(IllegalArgumentException.class, () -> edition.updateTitle("   "));

        assertEquals("Edition title", edition.getTitle());
    }

    @Test
    void shouldAssignNote() {
        var edition = new Edition("Edition title");
        edition.assign(new Note("Note title", "Note content"));

        assertEquals(1, edition.getNotes().size());
    }

    @Test
    void shouldNotAssignNoteToEditionThatIsNotOpen() {
        var edition = new Edition("Edition title");

        edition.assign(new Note("Note title", "Note content"));

        edition.close();

        assertThrows(IllegalStateException.class, () -> {
            edition.assign(new Note("Note title", "Note content"));
        });
    }

    @Test
    void shouldCloseEdition() {
        var edition = new Edition("Edition title");

        edition.assign(new Note("Note title", "Note content"));

        edition.close();

        assertEquals(EditionStatus.CLOSED, edition.getStatus());
    }

    @Test
    void shouldNotCloseEditionIfItHasNoNotesAssigned() {
        var edition = new Edition("Edition title");

        assertThrows(IllegalStateException.class, edition::close);
        assertEquals(EditionStatus.OPEN, edition.getStatus());
    }

    @Test
    void shouldNotCloseEditionIfItIsNotOpen() {
        var edition = new Edition("Edition title");
        edition.assign(new Note("Note title", "Note content"));
        edition.close();

        assertThrows(IllegalStateException.class, edition::close);
    }

    @Test
    void shouldSubmitEditionToReview() {
        var edition = new Edition("Edition title");
        edition.assign(new Note("Note title", "Note content"));
        edition.close();
        edition.submitToReview();

        assertEquals(EditionStatus.AVAILABLE_FOR_REVIEW, edition.getStatus());
    }

    @Test
    void shouldNotSubmitEditionToReviewIfEditionIsNotClosed() {
        var edition = new Edition("Edition title");

        assertThrows(IllegalStateException.class, edition::submitToReview);
        assertEquals(EditionStatus.OPEN, edition.getStatus());
    }

    @Test
    void shouldPublishEdition() {
    }

    @Test
    void shouldNotPublishEditionIfEditionIsNotApproved() {
    }
}
