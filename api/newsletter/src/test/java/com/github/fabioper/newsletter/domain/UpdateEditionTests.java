package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.author.AuthorId;
import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.events.EditionCategoryUpdated;
import com.github.fabioper.newsletter.domain.edition.events.EditionTitleUpdated;
import com.github.fabioper.newsletter.domain.editor.EditorId;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When updating edition")
public class UpdateEditionTests {
    @Test
    @DisplayName("should update fields correctly")
    void shouldUpdateFieldsCorrectly() {
        var category = new Category("Category");
        var edition = new Edition("Edition", new EditorId(), category);

        edition.updateTitle("Edited title");

        var otherCategory = new Category("Category 2");
        edition.updateCategory(otherCategory);

        assertEquals("Edited title", edition.getTitle());
        assertEquals(otherCategory, edition.getCategory());
        assertThat(edition.getDomainEvents(), hasItems(
            new EditionTitleUpdated(edition.getId().value(), "Edition", "Edited title"),
            new EditionCategoryUpdated(edition.getId().value(), category.getId().value(), otherCategory.getId().value())
        ));
    }

    @Test
    @DisplayName("should update fields correctly if edition is pending adjustments")
    void shouldUpdateFieldsIfEditionIsPendingAdjustments() {
        var category = new Category("Category");
        var edition = new Edition("Edition", new EditorId(), category);
        edition.addNote("Title", shortContent, new AuthorId(), new Editorial("Editorial"));

        edition.closeEdition();
        edition.submitToReview();
        edition.putUnderReview();
        edition.putAsPendingAdjustments();

        edition.updateTitle("Edited title");

        var otherCategory = new Category("Other category");
        edition.updateCategory(otherCategory);

        assertEquals("Edited title", edition.getTitle());
        assertEquals(otherCategory, edition.getCategory());
        assertThat(edition.getDomainEvents(), hasItems(
            new EditionTitleUpdated(edition.getId().value(), "Edition", "Edited title"),
            new EditionCategoryUpdated(edition.getId().value(), category.getId().value(), otherCategory.getId().value())
        ));
    }

    @Test
    @DisplayName("should not update if edition is published")
    void shouldNotUpdateIfEditionIsPublished() {
        var originalCategory = new Category("Category");
        var edition = new Edition("Edition", new EditorId(), originalCategory);

        edition.addNote("Note", "Content", new AuthorId(), new Editorial("Editorial"));

        edition.closeEdition();

        var otherCategory = new Category("Other category");

        assertThrows(IllegalStateException.class, () -> edition.updateTitle("Edited title"));
        assertThrows(IllegalStateException.class, () -> {
            edition.updateCategory(otherCategory);
        });

        assertEquals("Edition", edition.getTitle());
        assertEquals(originalCategory, edition.getCategory());

        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionTitleUpdated(edition.getId().value(), "Edition", "Edited title"),
            new EditionCategoryUpdated(
                edition.getId().value(),
                originalCategory.getId().value(),
                otherCategory.getId().value()
            )
        )));
    }
}
