package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.events.NoteAssignedToEditionEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When assigning note to edition")
public class WhenAssigningNoteToEditionTests {
    @Test
    @DisplayName("should assign note to edition if edition is in draft")
    void shouldAssignNoteIfEditionIsDraft() {
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");
        var note = author.createNote("Title", "Content", editorial);

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
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");

        edition.assignNote(author.createNote("Title", "Content", editorial));
        edition.assignNote(author.createNote("Title", "Content", editorial));

        edition.publish();

        var note = author.createNote("Title", "Content", editorial);

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
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");
        var note = author.createNote("Title", "Content", editorial);
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
