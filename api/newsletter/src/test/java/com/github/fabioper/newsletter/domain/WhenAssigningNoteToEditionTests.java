package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.events.NoteAssignedToEditionEvent;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.note.Note;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When assigning note to edition")
public class WhenAssigningNoteToEditionTests {
    @Test
    @DisplayName("should assign note to edition if edition is in draft")
    void shouldAssignNoteIfEditionIsDraft() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");
        var note = new Note("Title", "Content", UUID.randomUUID(), editorial);

        edition.assignNote(note);

        assertEquals(1, edition.getNotes().size());
        assertEquals(note, edition.getNotes().get(0));
        assertEquals(edition, note.getEdition());

        assertThat(
            edition.getDomainEvents(),
            hasItems(new NoteAssignedToEditionEvent(note.getId(), edition.getId()))
        );
    }

    @Test
    @DisplayName("should throw exception if edition is published")
    void shouldThrowIfEditionIsPublished() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");

        edition.assignNote(new Note("Title", "Content", UUID.randomUUID(), editorial));
        edition.assignNote(new Note("Title", "Content", UUID.randomUUID(), editorial));

        edition.publish();

        var note = new Note("Title", "Content", UUID.randomUUID(), editorial);

        assertThrows(IllegalStateException.class, () -> edition.assignNote(note));
        assertNull(note.getEdition());

        assertThat(
            edition.getDomainEvents(),
            not(hasItems(new NoteAssignedToEditionEvent(note.getId(), edition.getId())))
        );
    }

    @Test
    @DisplayName("should throw exception if note is already assigned to edition")
    void shouldThrowIfNoteIsAlreadyAssigned() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");
        var note = new Note("Title", "Content", UUID.randomUUID(), editorial);
        edition.assignNote(note);

        edition.clearEvents();

        assertThrows(IllegalArgumentException.class, () -> edition.assignNote(note));
        assertEquals(edition, note.getEdition());

        assertThat(
            edition.getDomainEvents(),
            not(hasItems(new NoteAssignedToEditionEvent(note.getId(), edition.getId())))
        );
    }
}
