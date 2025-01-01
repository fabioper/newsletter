package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.note.AuthorId;
import com.github.fabioper.newsletter.domain.note.Note;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EditionTest {

    @Test
    void shouldCreateEditionWithOpenStatus() {
        var editorId = new EditorId();
        var edition = new Edition("Edition title", editorId);
        assertEquals("Edition title", edition.getTitle());
        assertEquals(editorId, edition.getEditorId());
        assertEquals(EditionStatus.OPEN, edition.getStatus());
    }

    @Test
    void shouldNotCreateEditionWithEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Edition("", new EditorId()));
        assertThrows(IllegalArgumentException.class, () -> new Edition(null, new EditorId()));
        assertThrows(IllegalArgumentException.class, () -> new Edition("  ", new EditorId()));
    }

    @Test
    void shouldNotCreateEditionWithInvalidEditorId() {
        assertThrows(IllegalArgumentException.class, () -> new Edition("Edition title", null));
    }

    @Test
    void shouldUpdateTitle() {
        var edition = new Edition("Edition title", new EditorId());
        edition.updateTitle("Edition title updated");

        assertEquals("Edition title updated", edition.getTitle());
    }

    @Test
    void shouldNotUpdateTitleWithEmptyTitle() {
        var edition = new Edition("Edition title", new EditorId());

        assertThrows(IllegalArgumentException.class, () -> edition.updateTitle(""));
        assertThrows(IllegalArgumentException.class, () -> edition.updateTitle(null));
        assertThrows(IllegalArgumentException.class, () -> edition.updateTitle("   "));

        assertEquals("Edition title", edition.getTitle());
    }

    @Test
    void shouldAssignNote() {
        var edition = new Edition("Edition title", new EditorId());
        edition.assign(new Note("Note title", "Note content", new AuthorId()));

        assertEquals(1, edition.getNotes().size());
    }

    @Test
    void shouldNotAssignNoteToEditionThatIsNotOpen() {
        var edition = new Edition("Edition title", new EditorId());

        edition.assign(new Note("Note title", "Note content", new AuthorId()));

        edition.close();

        assertThrows(IllegalStateException.class, () -> {
            edition.assign(new Note("Note title", "Note content", new AuthorId()));
        });
    }

    @Test
    void shouldCloseEdition() {
        var edition = new Edition("Edition title", new EditorId());

        edition.assign(new Note("Note title", "Note content", new AuthorId()));

        edition.close();

        assertEquals(EditionStatus.CLOSED, edition.getStatus());
    }

    @Test
    void shouldNotCloseEditionIfItHasNoNotesAssigned() {
        var edition = new Edition("Edition title", new EditorId());

        assertThrows(IllegalStateException.class, edition::close);
        assertEquals(EditionStatus.OPEN, edition.getStatus());
    }

    @Test
    void shouldNotCloseEditionIfItIsNotOpen() {
        var edition = new Edition("Edition title", new EditorId());
        edition.assign(new Note("Note title", "Note content", new AuthorId()));
        edition.close();

        assertThrows(IllegalStateException.class, edition::close);
    }

    @Test
    void shouldNotCloseEditionIfItsTotalReadingTimeExceedsLimit() {
        var edition = new Edition("Edition title", new EditorId());

        edition.assign(new Note("Note title", longContent, new AuthorId()));
        edition.assign(new Note("Note title", longContent, new AuthorId()));
        edition.assign(new Note("Note title", longContent, new AuthorId()));

        assertThrows(IllegalStateException.class, edition::close);
    }

    @Test
    void shouldSubmitEditionToReview() {
        var edition = new Edition("Edition title", new EditorId());
        edition.assign(new Note("Note title", "Note content", new AuthorId()));
        edition.close();
        edition.submitToReview();

        assertEquals(EditionStatus.AVAILABLE_FOR_REVIEW, edition.getStatus());
    }

    @Test
    void shouldNotSubmitEditionToReviewIfEditionIsNotClosed() {
        var edition = new Edition("Edition title", new EditorId());

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
