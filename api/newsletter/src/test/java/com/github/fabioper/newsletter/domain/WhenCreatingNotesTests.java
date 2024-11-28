package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When creating notes")
class WhenCreatingNotesTests {
    @Test
    @DisplayName("should create if data is valid")
    void shouldCreateNoteIfDataIsValid() {
        var author = new Author();
        var editorial = new Editorial("Test");
        var note = author.createNote("Note title", "Note content", editorial);

        assertNotNull(note.getId());
        assertEquals("Note title", note.getTitle());
        assertEquals("Note content", note.getContent());
        assertEquals(author, note.getAuthor());
        assertEquals(editorial, note.getEditorial());
        assertNotNull(note.getReadingTime());
    }

    @Test
    @DisplayName("should throw exception if provided data is invalid")
    void shouldThrowIfDataIsInvalid() {
        var author = new Author();
        var editorial = new Editorial("Test");

        assertThrows(
            IllegalArgumentException.class,
            () -> author.createNote("Title", "Content", null)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> author.createNote(null, "Content", editorial)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> author.createNote("Title", null, editorial)
        );
    }
}
