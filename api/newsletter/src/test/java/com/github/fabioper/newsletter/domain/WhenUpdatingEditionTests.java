package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.Author;
import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.events.EditionCategoryUpdated;
import com.github.fabioper.newsletter.domain.edition.events.EditionTitleUpdated;
import com.github.fabioper.newsletter.domain.editor.Editor;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When updating edition")
public class WhenUpdatingEditionTests {
    @Test
    @DisplayName("should update fields correctly")
    void shouldUpdateFieldsCorrectly() {
        var editor = new Editor();
        var category = new Category("Category");
        var edition = editor.createEdition("Edition", category);

        edition.updateTitle("Edited title");

        var otherCategory = new Category("Other");
        edition.changeCategory(otherCategory);

        assertEquals("Edited title", edition.getTitle());
        assertEquals(otherCategory, edition.getCategory());
        assertThat(edition.getDomainEvents(), hasItems(
            new EditionTitleUpdated(edition.getId(), "Edition", "Edited title"),
            new EditionCategoryUpdated(edition.getId(), category.getId(), otherCategory.getId())
        ));
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

        var otherCategory = new Category("Other");

        assertThrows(IllegalStateException.class, () -> edition.updateTitle("Edited title"));
        assertThrows(IllegalStateException.class, () -> {
            edition.changeCategory(otherCategory);
        });

        assertEquals("Edition", edition.getTitle());
        assertEquals(originalCategory, edition.getCategory());

        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionTitleUpdated(edition.getId(), "Edition", "Edited title"),
            new EditionCategoryUpdated(edition.getId(), originalCategory.getId(), otherCategory.getId())
        )));
    }
}
