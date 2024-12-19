package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.AuthorId;
import com.github.fabioper.newsletter.domain.category.CategoryId;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.ReadingTime;
import com.github.fabioper.newsletter.domain.edition.events.NoteContentUpdatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteEditorialUpdatedEvent;
import com.github.fabioper.newsletter.domain.edition.events.NoteTitleUpdatedEvent;
import com.github.fabioper.newsletter.domain.editor.EditorId;
import com.github.fabioper.newsletter.domain.editorial.EditorialId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        var edition = new Edition("Title", new EditorId(), new CategoryId());

        var editorialId = new EditorialId();
        var noteId = edition.addNote("Title", shortContent, new AuthorId(), editorialId);

        var newEditorial = new EditorialId();
        edition.updateNote(noteId, "New title", longContent, newEditorial);

        var note = edition.getNotes().get(0);

        assertEquals("New title", note.getTitle());
        assertEquals(longContent, note.getContent());
        assertEquals(newEditorial, note.getEditorialId());
        assertEquals(ReadingTime.from(longContent), note.getReadingTime());

        assertThat(
            note.getDomainEvents(),
            hasItems(
                new NoteTitleUpdatedEvent(noteId.value(), "Title", "New title"),
                new NoteContentUpdatedEvent(noteId.value(), shortContent, longContent),
                new NoteEditorialUpdatedEvent(noteId.value(), editorialId.value(), newEditorial.value())
            )
        );
    }

    @Test
    @DisplayName("should not update fields if edition is in published state")
    void shouldNotUpdateIfEditionIsPublished() {
        var edition = new Edition("Title", new EditorId(), new CategoryId());

        var editorial = new EditorialId();
        var noteId = edition.addNote("Title", shortContent, new AuthorId(), editorial);

        edition.closeEdition();

        var note = edition.getNotes().get(0);

        assertThrows(IllegalStateException.class, () -> {
            edition.updateNote(noteId, "New title", longContent, new EditorialId());
        });

        assertEquals("Title", note.getTitle());
        assertEquals(shortContent, note.getContent());
        assertEquals(editorial, note.getEditorialId());
        assertEquals(ReadingTime.from(shortContent), note.getReadingTime());
    }
}