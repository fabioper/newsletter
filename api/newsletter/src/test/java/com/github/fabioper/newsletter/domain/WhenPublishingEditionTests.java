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

        edition.publish();

        assertEquals(Status.PUBLISHED, edition.getStatus());
        assertNotNull(edition.getPublicationDate());
    }

    @Test
    @DisplayName("should throw if edition is already published")
    void shouldThrowIfEditionAlreadyPublished() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        edition.publish();

        assertThrows(IllegalStateException.class, edition::publish);
    }

    @Test
    @DisplayName("should throw exception if total reading time is greater than 8 minutes")
    void shouldThrowIfTotalReadingTimeIsGreaterThan8Minutes() {
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
    }
}
