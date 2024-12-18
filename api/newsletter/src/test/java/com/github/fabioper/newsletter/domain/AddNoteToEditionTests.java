package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.AuthorId;
import com.github.fabioper.newsletter.domain.category.CategoryId;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.events.NoteAddedToEdition;
import com.github.fabioper.newsletter.domain.editor.EditorId;
import com.github.fabioper.newsletter.domain.editorial.EditorialId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When assigning note to edition")
public class AddNoteToEditionTests {
    @Test
    @DisplayName("should add note to edition")
    void shouldAddNoteToEdition() {
        var edition = new Edition("Edition", new EditorId(), new CategoryId());
        var noteId = edition.addNote("Title", shortContent, new AuthorId(), new EditorialId());

        assertEquals(1, edition.getNotes().size());
        assertThat(
            edition.getDomainEvents(),
            hasItems(new NoteAddedToEdition(noteId.value(), edition.getId().value()))
        );
    }

    @Test
    @DisplayName("should not add note if edition is published")
    void shouldNotAddNoteToEditionIfEditionIsPublished() {
        var edition = new Edition("Edition", new EditorId(), new CategoryId());

        edition.addNote("Title", "Content", new AuthorId(), new EditorialId());
        edition.addNote("Title", "Content", new AuthorId(), new EditorialId());

        edition.closeEdition();

        assertThrows(
            IllegalStateException.class,
            () -> edition.addNote("Title", "Content", new AuthorId(), new EditorialId())
        );
    }
}
