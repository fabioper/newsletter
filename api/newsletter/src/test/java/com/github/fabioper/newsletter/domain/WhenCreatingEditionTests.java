package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When creating edition")
class WhenCreatingEditionTests {

    @Test
    @DisplayName("should create as a draft")
    void shouldCreateEditionAsADraft() {
        var editor = new Editor();
        var category = new Category("Test");
        var edition = editor.createEdition("Edition", category);

        assertNotNull(edition.getId());
        assertEquals("Edition", edition.getTitle());
        assertEquals(Status.DRAFT, edition.getStatus());
        assertEquals(category, edition.getCategory());
        assertEquals(editor, edition.getEditor());
        assertNull(edition.getPublicationDate());
    }

    @Test
    @DisplayName("should throw exception if provided data is invalid")
    void shouldThrowIfInvalidDataIsProvided() {
        var editor = new Editor();
        assertThrows(IllegalArgumentException.class, () -> editor.createEdition("Edition", null));
        assertThrows(IllegalArgumentException.class, () -> editor.createEdition(null, new Category("Category")));
    }
}
