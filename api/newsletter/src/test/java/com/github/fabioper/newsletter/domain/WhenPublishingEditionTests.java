package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When publishing edition")
public class WhenPublishingEditionTests {
    @Test
    @DisplayName("should update status and publication date")
    void shouldUpdateStatusAndPublicationDate() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        var author = new Author();
        var editorial = new Editorial("Editorial");

        edition.assignNote(author.createNote("Note 1", longContent, editorial));
        edition.assignNote(author.createNote("Note 2", longContent, editorial));

        edition.publish();

        assertEquals(Status.PUBLISHED, edition.getStatus());
        assertNotNull(edition.getPublicationDate());
    }

    @Test
    @DisplayName("should not publish if edition was already published")
    void shouldNotPublishIfEditionAlreadyPublished() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        assertThrows(IllegalStateException.class, edition::publish);
    }

    @Test
    @DisplayName("should not publish if total reading time exceeds limit")
    void shouldNotPublishIfTotalReadingTimeExceedsLimit() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

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
    }

    @Test
    @DisplayName("should not publish if edition has no notes")
    void shouldNotPublishIfEditionHasNoNotes() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        assertThrows(IllegalStateException.class, edition::publish);
        assertEquals(Status.DRAFT, edition.getStatus());
        assertNull(edition.getPublicationDate());
    }
}
