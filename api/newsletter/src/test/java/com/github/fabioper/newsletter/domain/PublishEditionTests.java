package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PublishEditionTests {
    @Test
    void editionShouldBePublished() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        edition.publish();

        assertEquals(Status.PUBLISHED, edition.getStatus(), "Edition status should be published");
        assertNotNull(edition.getPublicationDate(), "Publication date should be assigned");
    }

    @Test
    void publishedEditionShouldNotBePublished() {
        var editor = new Editor();
        var edition = editor.createEdition(new Category("Category"));

        edition.publish();

        assertThrows(IllegalStateException.class, edition::publish,
                     "Should throw exception when trying to published an already published edition");
    }
}
