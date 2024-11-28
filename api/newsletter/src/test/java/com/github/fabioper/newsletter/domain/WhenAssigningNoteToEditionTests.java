package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When assigning note to edition")
public class WhenAssigningNoteToEditionTests {
    @Test
    @DisplayName("should assign note to edition if edition is in draft")
    void shouldAssignNoteIfEditionIsDraft() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");
        var note = author.createNote("Title", "Content", editorial);

        edition.assignNote(note);

        assertEquals(1, edition.getNotes().size());
        assertEquals(note, edition.getNotes().get(0));
    }

    @Test
    @DisplayName("should throw exception if edition is published")
    void shouldThrowIfEditionIsPublished() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        edition.publish();

        var author = new Author();
        var editorial = new Editorial("Editorial");
        var note = author.createNote("Title", "Content", editorial);

        assertThrows(IllegalStateException.class, () -> edition.assignNote(note));
    }
}
