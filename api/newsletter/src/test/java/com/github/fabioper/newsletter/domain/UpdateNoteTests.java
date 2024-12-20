package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.ReadingTime;
import com.github.fabioper.newsletter.domain.edition.events.NoteContentUpdatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteEditorialUpdatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteTitleUpdatedEvent;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When updating notes")
public class UpdateNoteTests {
    @Test
    @DisplayName("should update fields correctly if edition is in draft state")
    void shouldUpdateFieldsCorrectlyIfEditionIsDraft() {
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");
        var noteId = edition.addNote("Title", shortContent, UUID.randomUUID(), editorial);

        var newEditorial = new Editorial("New editorial");
        edition.updateNote(noteId, "New title", longContent, newEditorial);

        var note = edition.getNotes().get(0);

        assertEquals("New title", note.getTitle());
        assertEquals(longContent, note.getContent());
        assertEquals(newEditorial, note.getEditorial());
        assertEquals(ReadingTime.from(longContent), note.getReadingTime());

        assertThat(
            note.getDomainEvents(),
            hasItems(
                new NoteTitleUpdatedEvent(noteId, "Title", "New title"),
                new NoteContentUpdatedEvent(noteId, shortContent, longContent),
                new NoteEditorialUpdatedEvent(noteId, editorial.getId(), newEditorial.getId())
            )
        );
    }

    @Test
    @DisplayName("should update fields correctly if edition is pending adjustments")
    void shouldUpdateFieldsCorrectlyIfEditionIsPendingAdjustments() {
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");
        var noteId = edition.addNote("Title", shortContent, UUID.randomUUID(), editorial);

        edition.closeEdition();
        edition.submitToReview();
        edition.putUnderReview();
        edition.putAsPendingAdjustments();

        var newEditorial = new Editorial("New editorial");
        edition.updateNote(noteId, "New title", longContent, newEditorial);

        var note = edition.getNotes().get(0);

        assertEquals("New title", note.getTitle());
        assertEquals(longContent, note.getContent());
        assertEquals(newEditorial, note.getEditorial());
        assertEquals(ReadingTime.from(longContent), note.getReadingTime());

        assertThat(
            note.getDomainEvents(),
            hasItems(
                new NoteTitleUpdatedEvent(noteId, "Title", "New title"),
                new NoteContentUpdatedEvent(noteId, shortContent, longContent),
                new NoteEditorialUpdatedEvent(noteId, editorial.getId(), newEditorial.getId())
            )
        );
    }

    @Test
    @DisplayName("should not update fields if edition is in published state")
    void shouldNotUpdateIfEditionIsPublished() {
        var edition = new Edition("Title", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");
        var noteId = edition.addNote("Title", shortContent, UUID.randomUUID(), editorial);

        edition.closeEdition();

        var note = edition.getNotes().get(0);

        assertThrows(IllegalStateException.class, () -> {
            edition.updateNote(noteId, "New title", longContent, new Editorial("Editorial"));
        });

        assertEquals("Title", note.getTitle());
        assertEquals(shortContent, note.getContent());
        assertEquals(editorial, note.getEditorial());
        assertEquals(ReadingTime.from(shortContent), note.getReadingTime());
    }
}
