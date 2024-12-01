package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.Author;
import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.edition.events.EditionPublishedEvent;
import com.github.fabioper.newsletter.domain.editor.Editor;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When publishing edition")
public class WhenPublishingEditionTests {
    @Test
    @DisplayName("should update status and publication date")
    void shouldUpdateStatusAndPublicationDate() {
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");

        edition.assignNote(author.createNote("Note 1", longContent, editorial));
        edition.assignNote(author.createNote("Note 2", longContent, editorial));

        edition.publish();

        assertEquals(Status.PUBLISHED, edition.getStatus());
        assertNotNull(edition.getPublicationDate());

        assertThat(edition.getDomainEvents(), hasItems(
            new EditionPublishedEvent(edition.getId())
        ));
    }

    @Test
    @DisplayName("should not publish if edition was already published")
    void shouldNotPublishIfEditionAlreadyPublished() {
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        assertThrows(IllegalStateException.class, edition::publish);
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionPublishedEvent(edition.getId())
        )));
    }

    @Test
    @DisplayName("should not publish if total reading time exceeds limit")
    void shouldNotPublishIfTotalReadingTimeExceedsLimit() {
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");

        edition.updateNotes(List.of(
            author.createNote("Note 1", longContent, editorial),
            author.createNote("Note 2", longContent, editorial),
            author.createNote("Note 3", longContent, editorial),
            author.createNote("Note 4", longContent, editorial)
        ));

        assertThrows(IllegalStateException.class, edition::publish);
        assertEquals(Status.DRAFT, edition.getStatus());
        assertNull(edition.getPublicationDate());
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionPublishedEvent(edition.getId())
        )));
    }

    @Test
    @DisplayName("should not publish if edition has no notes")
    void shouldNotPublishIfEditionHasNoNotes() {
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        assertThrows(IllegalStateException.class, edition::publish);
        assertEquals(Status.DRAFT, edition.getStatus());
        assertNull(edition.getPublicationDate());
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionPublishedEvent(edition.getId())
        )));
    }
}
