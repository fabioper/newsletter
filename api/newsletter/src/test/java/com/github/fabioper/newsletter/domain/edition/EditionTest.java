package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.note.AuthorId;
import com.github.fabioper.newsletter.domain.note.Note;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static org.junit.jupiter.api.Assertions.*;

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

        var note = new Note("Note title", "Note content", new AuthorId());
        note.close();

        edition.assign(note);

        edition.close();

        assertThrows(IllegalStateException.class, () -> {
            edition.assign(new Note("Other note title", "Note content", new AuthorId()));
        });
    }

    @Test
    void shouldCloseEdition() {
        var edition = new Edition("Edition title", new EditorId());

        var note = new Note("Note title", "Note content", new AuthorId());
        note.close();

        edition.assign(note);

        edition.close();

        assertEquals(EditionStatus.CLOSED, edition.getStatus());
    }

    @Test
    void shouldNotCloseEditionUntilAllNotesAreClosed() {
        var edition = new Edition("Edition title", new EditorId());
        var note1 = new Note("Title", "Content", new AuthorId());
        var note2 = new Note("Title", "Content", new AuthorId());
        var note3 = new Note("Title", "Content", new AuthorId());

        note1.close();
        note2.close();

        edition.assign(note1);
        edition.assign(note2);
        edition.assign(note3);

        assertThrows(IllegalStateException.class, edition::close);
        assertEquals(EditionStatus.OPEN, edition.getStatus());

        note3.close();
        assertDoesNotThrow(edition::close);
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
        var note = new Note("Note title", "Note content", new AuthorId());
        note.close();
        edition.assign(note);
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
        var edition = createEditionAvailableForReview();

        assertEquals(EditionStatus.AVAILABLE_FOR_REVIEW, edition.getStatus());
    }

    @Test
    void shouldPutEditionUnderReview() {
        var edition = createEditionAvailableForReview();
        edition.putUnderReview();
        assertEquals(EditionStatus.UNDER_REVIEW, edition.getStatus());
    }

    @Test
    void shouldNotPutEditionUnderReviewIfEditionIsNotAvailableForReview() {
        var edition = new Edition("Title", new EditorId());
        assertThrows(IllegalStateException.class, edition::putUnderReview);
        assertEquals(EditionStatus.OPEN, edition.getStatus());

        var note = new Note("Title", "Content", new AuthorId());
        note.close();
        edition.assign(note);
        edition.close();

        assertThrows(IllegalStateException.class, edition::putUnderReview);
        assertEquals(EditionStatus.CLOSED, edition.getStatus());

        edition.submitToReview();
        edition.putUnderReview();
        assertEquals(EditionStatus.UNDER_REVIEW, edition.getStatus());
    }

    @Test
    void shouldNotSubmitEditionToReviewIfEditionIsNotClosed() {
        var edition = new Edition("Edition title", new EditorId());

        assertThrows(IllegalStateException.class, edition::submitToReview);
        assertEquals(EditionStatus.OPEN, edition.getStatus());
    }

    @Test
    void shoudMakeEditionAvailableForPublication() {
        var edition = createEditionAvailableForReview();
        edition.putUnderReview();
        edition.makeAvailableForPublication();
        assertEquals(EditionStatus.AVAILABLE_FOR_PUBLICATION, edition.getStatus());
    }

    @Test
    void shouldNotMakeEditionAvailableForPublicationIfEditionIsNotUnderReview() {
        var edition = createEditionAvailableForReview();
        assertThrows(IllegalStateException.class, edition::makeAvailableForPublication);
        assertEquals(EditionStatus.AVAILABLE_FOR_REVIEW, edition.getStatus());
    }

    private static Edition createEditionAvailableForReview() {
        var edition = new Edition("Edition title", new EditorId());

        var note = new Note("Note title", "Note content", new AuthorId());
        note.close();
        edition.assign(note);

        edition.close();
        edition.submitToReview();

        return edition;
    }
}
