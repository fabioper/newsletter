package com.github.fabioper.newsletter.domain.note;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {
    @Test
    void shouldCreateNoteWithOpenStatus() {
        var authorId = new AuthorId();
        var note = new Note("Note title", "Note content", authorId);

        assertEquals("Note title", note.getTitle());
        assertEquals("Note content", note.getContent());
        assertEquals(NoteStatus.OPEN, note.getStatus());
        assertEquals(authorId, note.getAuthorId());
        assertNotNull(note.getId());
    }

    @Test
    void shouldNotCreateNoteWithEmptyArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Note(null, "Note content", new AuthorId()));
        assertThrows(IllegalArgumentException.class, () -> new Note("", "Note content", new AuthorId()));
        assertThrows(IllegalArgumentException.class, () -> new Note("  ", "Note content", new AuthorId()));
        assertThrows(IllegalArgumentException.class, () -> new Note("Note title", null, new AuthorId()));
        assertThrows(IllegalArgumentException.class, () -> new Note("Note title", "", new AuthorId()));
        assertThrows(IllegalArgumentException.class, () -> new Note("Note title", "  ", new AuthorId()));
        assertThrows(IllegalArgumentException.class, () -> new Note("Note title", "Note content", null));
    }

    @Test
    void shouldUpdateTitle() {
        var note = new Note("Note title", "Note content", new AuthorId());

        note.updateTitle("Note title updated");

        assertEquals("Note title updated", note.getTitle());
    }

    @Test
    void shouldNotUpdateTitleWithEmptyArgument() {
        var note = new Note("Note title", "Note content", new AuthorId());

        assertThrows(IllegalArgumentException.class, () -> note.updateTitle(null));
        assertThrows(IllegalArgumentException.class, () -> note.updateTitle(""));
        assertThrows(IllegalArgumentException.class, () -> note.updateTitle("   "));
    }

    @Test
    void shouldUpdateContent() {
        var note = new Note("Note content", "Note content", new AuthorId());

        note.updateContent("Note content updated");

        assertEquals("Note content updated", note.getContent());
    }

    @Test
    void shouldNotUpdateContentWithEmptyArgument() {
        var note = new Note("Note title", "Note content", new AuthorId());

        assertThrows(IllegalArgumentException.class, () -> note.updateContent(null));
        assertThrows(IllegalArgumentException.class, () -> note.updateContent(""));
        assertThrows(IllegalArgumentException.class, () -> note.updateContent("   "));
    }

    @Test
    void shouldNotUpdateTitleIfNoteIsClosed() {
        var note = new Note("Note title", "Note content", new AuthorId());
        note.close();

        assertThrows(IllegalStateException.class, () -> note.updateTitle("Updated"));
        assertEquals("Note title", note.getTitle());
    }

    @Test
    void shouldNotUpdateContentIfNoteIsClosed() {
        var note = new Note("Note title", "Note content", new AuthorId());
        note.close();

        assertThrows(IllegalStateException.class, () -> note.updateContent("Updated"));
        assertEquals("Note content", note.getContent());
    }

    @Test
    void shouldClose() {
        var note = new Note("Note title", "Note content", new AuthorId());
        note.close();

        assertEquals(NoteStatus.CLOSED, note.getStatus());
    }

    @Test
    void shouldNotCloseIfEditionIsNotOpen() {
        var note = new Note("Note title", "Note content", new AuthorId());
        note.close();

        assertThrows(IllegalStateException.class, note::close);
    }

    @Test
    void shouldOpen() {
        var note = new Note("Note title", "Note content", new AuthorId());

        note.close();
        assertEquals(NoteStatus.CLOSED, note.getStatus());

        note.open();
        assertEquals(NoteStatus.OPEN, note.getStatus());
    }

    @Test
    void shouldNotOpenIfEditionIsNotClosed() {
        var note = new Note("Note title", "Note content", new AuthorId());
        assertThrows(IllegalStateException.class, note::open);
        assertEquals(NoteStatus.OPEN, note.getStatus());
    }
}
