package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.note.Note;
import com.github.fabioper.newsletter.domain.note.ReadingTime;
import com.github.fabioper.newsletter.domain.note.events.NoteContentUpdatedEvent;
import com.github.fabioper.newsletter.domain.note.events.NoteEditorialUpdatedEvent;
import com.github.fabioper.newsletter.domain.note.events.NoteTitleUpdatedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When updating notes")
public class WhenUpdatingNotesTests {
    @Test
    @DisplayName("should update fields correctly if edition is in draft state")
    void shouldUpdateFieldsCorrectlyIfEditionIsDraft() {
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");
        var note = new Note("Title", shortContent, UUID.randomUUID(), editorial);

        edition.assignNote(note);

        note.updateTitle("New title");
        note.updateContent(longContent);

        var newEditorial = new Editorial("Other");
        note.updateEditorial(newEditorial);

        assertEquals("New title", note.getTitle());
        assertEquals(longContent, note.getContent());
        assertEquals(newEditorial, note.getEditorial());
        assertEquals(ReadingTime.from(longContent), note.getReadingTime());

        assertThat(
            note.getDomainEvents(),
            hasItems(
                new NoteTitleUpdatedEvent(note.getId(), "Title", "New title"),
                new NoteContentUpdatedEvent(note.getId(), shortContent, longContent),
                new NoteEditorialUpdatedEvent(note.getId(), editorial.getId(), newEditorial.getId())
            )
        );
    }

    @Test
    @DisplayName("should update fields correctly if note is not assigned to edition")
    void shouldUpdateCorrectlyIfNoteIsNotAssignedToEdition() {
        var editorial = new Editorial("Editorial");
        var note = new Note("Title", shortContent, UUID.randomUUID(), editorial);

        note.updateTitle("New title");
        note.updateContent(longContent);

        var newEditorial = new Editorial("Other");
        note.updateEditorial(newEditorial);

        assertEquals("New title", note.getTitle());
        assertEquals(longContent, note.getContent());
        assertEquals(newEditorial, note.getEditorial());
        assertEquals(ReadingTime.from(longContent), note.getReadingTime());

        assertThat(
            note.getDomainEvents(),
            hasItems(
                new NoteTitleUpdatedEvent(note.getId(), "Title", "New title"),
                new NoteContentUpdatedEvent(note.getId(), shortContent, longContent),
                new NoteEditorialUpdatedEvent(note.getId(), editorial.getId(), newEditorial.getId())
            )
        );
    }

    @Test
    @DisplayName("should not update fields if edition is in published state")
    void shouldNotUpdateIfEditionIsPublished() {
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");
        var note = new Note("Title", shortContent, UUID.randomUUID(), editorial);

        edition.assignNote(note);

        edition.publish();

        note.clearEvents();

        assertThrows(IllegalStateException.class, () -> note.updateTitle("New title"));
        assertThrows(IllegalStateException.class, () -> note.updateContent(longContent));
        assertThrows(IllegalStateException.class, () -> {
            var newEditorial = new Editorial("Other");
            note.updateEditorial(newEditorial);
        });

        assertEquals("Title", note.getTitle());
        assertEquals(shortContent, note.getContent());
        assertEquals(editorial, note.getEditorial());
        assertEquals(ReadingTime.from(shortContent), note.getReadingTime());

        assertThat(note.getDomainEvents(), empty());
    }
}
