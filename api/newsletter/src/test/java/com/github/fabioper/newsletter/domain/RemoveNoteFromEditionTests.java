package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When unassigning notes from edition")
public class RemoveNoteFromEditionTests {
    @Test
    @DisplayName("should remove it if note is assigned to edition")
    void shouldRemoveNoteIfPresent() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var note1 = edition.addNote("Title", "Content", UUID.randomUUID(), new Editorial("Editorial"));
        var note2 = edition.addNote("Title 2", "Content", UUID.randomUUID(), new Editorial("Editorial"));

        edition.removeNote(note2);

        assertEquals(1, edition.getNotes().size());
        assertEquals(note1, edition.getNotes().get(0).getId());
        assertFalse(edition.getNotes().stream().anyMatch(n -> n.getId().equals(note2)));
    }

    @Test
    @DisplayName("should throw exception if note is not assigned to edition")
    void shouldThrowIfNotPresent() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        edition.addNote("Title", "Content", UUID.randomUUID(), new Editorial("Editorial"));

        assertThrows(IllegalArgumentException.class, () -> edition.removeNote(UUID.randomUUID()));
    }

    @Test
    @DisplayName("should not remove if edition is published")
    void shouldNotRemoveIfEditionIsPublished() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var note1 = edition.addNote("Title", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        var note2 = edition.addNote("Title 2", longContent, UUID.randomUUID(), new Editorial("Editorial"));

        edition.closeEdition();

        assertThrows(IllegalStateException.class, () -> edition.removeNote(note1));
        assertThrows(IllegalStateException.class, () -> edition.removeNote(note2));

        assertTrue(edition.getNotes().stream()
                       .anyMatch(n -> n.getId().equals(note1) || n.getId().equals(note2)));
    }
}
