package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.note.Note;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When unassigning notes from edition")
public class WhenUnassigningNotesTests {
    @Test
    @DisplayName("should remove it if note is assigned to edition")
    void shouldRemoveNoteIfPresent() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var note1 = new Note("Title", "Content", UUID.randomUUID(), new Editorial("Editorial"));
        var note2 = new Note("Title 2", "Content", UUID.randomUUID(), new Editorial("Editorial"));

        edition.updateNotes(List.of(note1, note2));

        edition.unassignNote(note2);

        assertEquals(1, edition.getNotes().size());
        assertEquals(note1, edition.getNotes().get(0));
        assertFalse(edition.getNotes().contains(note2));
        assertFalse(note2.isLockedForChanges());
    }

    @Test
    @DisplayName("should throw exception if note is not assigned to edition")
    void shouldThrowIfNotPresent() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var note1 = new Note("Title", "Content", UUID.randomUUID(), new Editorial("Editorial"));
        var note2 = new Note("Title 2", "Content", UUID.randomUUID(), new Editorial("Editorial"));

        edition.assignNote(note1);

        assertThrows(IllegalArgumentException.class, () -> edition.unassignNote(note2));
    }

    @Test
    @DisplayName("should not unassign if edition is published")
    void shouldNotUnassignIfEditionIsPublished() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var note1 = new Note("Title", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        var note2 = new Note("Title 2", longContent, UUID.randomUUID(), new Editorial("Editorial"));

        edition.assignNote(note1);
        edition.assignNote(note2);

        edition.publish();

        assertThrows(IllegalArgumentException.class, () -> edition.unassignNote(note1));
        assertThrows(IllegalArgumentException.class, () -> edition.unassignNote(note2));
        assertThat(edition.getNotes(), hasItems(note1, note2));
    }
}
