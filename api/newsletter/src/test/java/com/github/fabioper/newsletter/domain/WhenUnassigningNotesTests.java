package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.Author;
import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.editor.Editor;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When unassigning notes from edition")
public class WhenUnassigningNotesTests {
    @Test
    @DisplayName("should remove it if note is assigned to edition")
    void shouldRemoveNoteIfPresent() {
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        var author = new Author();
        var note1 = author.createNote("Title", "Content", new Editorial("Editorial"));
        var note2 = author.createNote("Title 2", "Content", new Editorial("Editorial"));

        edition.updateNotes(List.of(note1, note2));

        edition.unassignNote(note2);

        assertEquals(1, edition.getNotes().size());
        assertEquals(note1, edition.getNotes().get(0));
        assertFalse(edition.getNotes().contains(note2));
        assertNull(note2.getEdition());
    }

    @Test
    @DisplayName("should throw exception if note is not assigned to edition")
    void shouldThrowIfNotPresent() {
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        var author = new Author();
        var note1 = author.createNote("Title", "Content", new Editorial("Editorial"));
        var note2 = author.createNote("Title 2", "Content", new Editorial("Editorial"));

        edition.assignNote(note1);

        assertThrows(IllegalArgumentException.class, () -> edition.unassignNote(note2));
        assertNotEquals(edition, note2.getEdition());
    }
}
