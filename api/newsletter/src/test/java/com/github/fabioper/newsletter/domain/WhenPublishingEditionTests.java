package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
