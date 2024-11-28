package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssignNotesTests {
    @Test
    void editionShouldAssignNote() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");
        var note = author.createNote("Title", "Content", editorial);
        edition.assignNote(note);

        assertEquals(1, edition.getNotes().size(), "Edition should contain 1 note");
        assertEquals(note, edition.getNotes().get(0), "Edition should contain correct note");
    }

    @Test
    void publishedEditionShouldNotAllowToAssignNotes() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        edition.publish();

        var author = new Author();
        var editorial = new Editorial("Editorial");
        var note = author.createNote("Title", "Content", editorial);

        assertThrows(IllegalStateException.class, () -> edition.assignNote(note),
                     "Should not be able to assign note to an edition with status Published");
    }
}
