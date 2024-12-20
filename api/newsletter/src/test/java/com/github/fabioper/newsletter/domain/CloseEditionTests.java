package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.edition.events.EditionClosedEvent;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When publishing edition")
public class CloseEditionTests {
    @Test
    @DisplayName("should update status and publication date")
    void shouldUpdateStatusAndPublicationDate() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        edition.addNote("Note 1", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.addNote("Note 2", longContent, UUID.randomUUID(), new Editorial("Editorial"));

        edition.closeEdition();

        assertEquals(Status.CLOSED, edition.getStatus());
        assertNotNull(edition.getPublicationDate());

        assertThat(edition.getDomainEvents(), hasItems(
            new EditionClosedEvent(edition.getId())
        ));
    }

    @Test
    @DisplayName("should not publish if edition was already published")
    void shouldNotPublishIfEditionAlreadyPublished() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        assertThrows(IllegalStateException.class, edition::closeEdition);
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionClosedEvent(edition.getId())
        )));
    }

    @Test
    @DisplayName("should not publish if total reading time exceeds limit")
    void shouldNotPublishIfTotalReadingTimeExceedsLimit() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        edition.addNote("Note 1", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.addNote("Note 2", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.addNote("Note 3", longContent, UUID.randomUUID(), new Editorial("Editorial"));
        edition.addNote("Note 4", longContent, UUID.randomUUID(), new Editorial("Editorial"));

        assertThrows(IllegalStateException.class, edition::closeEdition);

        assertEquals(Status.DRAFT, edition.getStatus());

        assertNull(edition.getPublicationDate());
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionClosedEvent(edition.getId())
        )));
    }

    @Test
    @DisplayName("should not publish if edition has no notes")
    void shouldNotPublishIfEditionHasNoNotes() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        assertThrows(IllegalStateException.class, edition::closeEdition);
        assertEquals(Status.DRAFT, edition.getStatus());
        assertNull(edition.getPublicationDate());
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionClosedEvent(edition.getId())
        )));
    }
}
