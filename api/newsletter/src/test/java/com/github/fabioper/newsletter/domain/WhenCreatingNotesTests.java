package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.note.Note;
import com.github.fabioper.newsletter.domain.note.events.NoteCreatedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When creating notes")
class WhenCreatingNotesTests {
    @Test
    @DisplayName("should create if data is valid")
    void shouldCreateNoteIfDataIsValid() {
        var editorial = new Editorial("Test");
        var authorId = UUID.randomUUID();
        var note = new Note("Note title", "Note content", authorId, editorial);

        assertNotNull(note.getId());
        assertEquals("Note title", note.getTitle());
        assertEquals("Note content", note.getContent());
        assertEquals(authorId, note.getAuthorId());
        assertEquals(editorial, note.getEditorial());
        assertNotNull(note.getReadingTime());

        var noteCreatedEvent = new NoteCreatedEvent(note.getId());
        assertThat(note.getDomainEvents(), hasItem(noteCreatedEvent));
    }

    @Test
    @DisplayName("should throw exception if provided data is invalid")
    void shouldThrowIfDataIsInvalid() {
        var editorial = new Editorial("Test");

        assertThrows(
            IllegalArgumentException.class,
            () -> new Note("Title", "Content", UUID.randomUUID(), null)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new Note(null, "Content", UUID.randomUUID(), editorial)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new Note("Title", null, UUID.randomUUID(), editorial)
        );
    }
}
