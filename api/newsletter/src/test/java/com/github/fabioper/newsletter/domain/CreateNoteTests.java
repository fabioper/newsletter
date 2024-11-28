package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateNoteTests {
    @Test
    void authorShouldBeAbleToCreateNoteWithValidData() {
        var author = new Author();
        var editorial = new Editorial("Test");
        var note = author.createNote("Note title", "Note content", editorial);

        assertNotNull(note.getId(), "Note should have an id");
        assertEquals("Note title", note.getTitle(),
                     "Note should have the correct title");
        assertEquals("Note content", note.getContent(),
                     "Note should have the correct content");
        assertEquals(author, note.getAuthor(),
                     "Note should have the correct author");
        assertEquals(editorial, note.getEditorial(),
                     "Note should have the correct editorial");
        assertNotNull(note.getReadingTime(), "reading time should be set");
    }

    @Test
    void authorShouldBeAbleToCreateNoteWithInvalidData() {
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
