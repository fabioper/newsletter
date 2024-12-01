package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.edition.events.EditionCreatedEvent;
import com.github.fabioper.newsletter.domain.editor.Editor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
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

        assertThat(edition.getDomainEvents(), hasItems(
            new EditionCreatedEvent(edition.getId())
        ));
    }

    @Test
    @DisplayName("should throw exception if provided data is invalid")
    void shouldThrowIfInvalidDataIsProvided() {
        var editor = new Editor();
        assertThrows(IllegalArgumentException.class, () -> editor.createEdition("Edition", null));
        assertThrows(IllegalArgumentException.class, () -> editor.createEdition(null, new Category("Category")));
    }
}
