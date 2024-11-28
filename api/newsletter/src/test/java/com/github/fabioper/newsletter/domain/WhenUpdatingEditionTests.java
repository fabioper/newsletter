package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When updating edition")
public class WhenUpdatingEditionTests {
    @Test
    @DisplayName("should update fields correctly")
    void shouldUpdateFieldsCorrectly() {
        var editor = new Editor();
        var edition = editor.createEdition("Edition", new Category("Category"));

        edition.updateTitle("Edited title");

        var otherCategory = new Category("Other");
        edition.changeCategory(otherCategory);

        assertEquals("Edited title", edition.getTitle());
        assertEquals(otherCategory, edition.getCategory());
    }

    @Test
    @DisplayName("should not update if edition is published")
    void shouldNotUpdateIfEditionIsPublished() {
        var editor = new Editor();
        var originalCategory = new Category("Category");
        var edition = editor.createEdition("Edition", originalCategory);

        var author = new Author();
        var editorial = new Editorial("Editorial");

        edition.assignNote(author.createNote("Note", "Content", editorial));

        edition.publish();

        assertThrows(IllegalStateException.class, () -> edition.updateTitle("Edited title"));
        assertThrows(IllegalStateException.class, () -> {
            var otherCategory = new Category("Other");
            edition.changeCategory(otherCategory);
        });

        assertEquals("Edition", edition.getTitle());
        assertEquals(originalCategory, edition.getCategory());
    }
}
