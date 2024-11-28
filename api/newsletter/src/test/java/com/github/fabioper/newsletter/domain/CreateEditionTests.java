package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateEditionTests {

    @Test
    void editorShouldBeAbleToCreateEdition() {
        var editor = new Editor();
        var category = new Category("Test");
        var edition = editor.createEdition(category);

        assertNotNull(edition.getId(), "Edition should generate a new id");
        assertEquals(Status.DRAFT, edition.getStatus(), "Edition should be created with status DRAFT");
        assertEquals(category, edition.getCategory(), "Edition should be created with the correct category");
        assertEquals(editor, edition.getEditor(), "Edition should be created with the correct editor");
        assertNull(edition.getPublicationDate(), "Edition should not set publication date");
    }

    @Test
    void editorShouldNotBeAbleToCreateEditionWithInvalidCategory() {
        var editor = new Editor();
        assertThrows(IllegalArgumentException.class, () -> editor.createEdition(null));
    }
}
