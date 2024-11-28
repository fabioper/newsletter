package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When updating notes")
public class WhenUpdatingNotesTests {
    @Test
    @DisplayName("should update fields correctly if edition is in draft state")
    void shouldUpdateFieldsCorrectlyIfEditionIsDraft() {
        var editor = new Editor();
        var edition = editor.createEdition("Title", new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");
        var note = author.createNote("Title", shortContent, editorial);

        edition.assignNote(note);

        note.updateTitle("New title");
        note.updateContent(longContent);

        var newEditorial = new Editorial("Other");
        note.changeEditorial(newEditorial);

        assertEquals("New title", note.getTitle());
        assertEquals(longContent, note.getContent());
        assertEquals(newEditorial, note.getEditorial());
        assertEquals(ReadingTime.from(longContent), note.getReadingTime());
    }

    @Test
    @DisplayName("should update fields correctly if note is not assigned to edition")
    void shouldUpdateCorrectlyIfNoteIsNotssignedToEdition() {
        var author = new Author();
        var editorial = new Editorial("Editorial");
        var note = author.createNote("Title", shortContent, editorial);

        note.updateTitle("New title");
        note.updateContent(longContent);

        var newEditorial = new Editorial("Other");
        note.changeEditorial(newEditorial);

        assertEquals("New title", note.getTitle());
        assertEquals(longContent, note.getContent());
        assertEquals(newEditorial, note.getEditorial());
        assertEquals(ReadingTime.from(longContent), note.getReadingTime());
    }

    @Test
    @DisplayName("should not update fields if edition is in published state")
    void shouldNotUpdateIfEditionIsPublished() {
        var editor = new Editor();
        var edition = editor.createEdition("Title", new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");
        var note = author.createNote("Title", shortContent, editorial);

        edition.assignNote(note);

        edition.publish();

        assertThrows(IllegalStateException.class, () -> note.updateTitle("New title"));
        assertThrows(IllegalStateException.class, () -> note.updateContent(longContent));
        assertThrows(IllegalStateException.class, () -> {
            var newEditorial = new Editorial("Other");
            note.changeEditorial(newEditorial);
        });

        assertEquals("Title", note.getTitle());
        assertEquals(shortContent, note.getContent());
        assertEquals(editorial, note.getEditorial());
        assertEquals(ReadingTime.from(shortContent), note.getReadingTime());
    }
}
