package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.CategoryId;
import com.github.fabioper.newsletter.domain.edition.AuthorId;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.EditorId;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.edition.events.EditionClosedEvent;
import com.github.fabioper.newsletter.domain.editorial.EditorialId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        var edition = new Edition("Edition", new EditorId(), new CategoryId());

        edition.addNote("Note 1", longContent, new AuthorId(), new EditorialId());
        edition.addNote("Note 2", longContent, new AuthorId(), new EditorialId());

        edition.closeEdition();

        assertEquals(Status.CLOSED, edition.getStatus());
        assertNotNull(edition.getPublicationDate());

        assertThat(edition.getDomainEvents(), hasItems(
            new EditionClosedEvent(edition.getId().value())
        ));
    }

    @Test
    @DisplayName("should not publish if edition was already published")
    void shouldNotPublishIfEditionAlreadyPublished() {
        var edition = new Edition("Edition", new EditorId(), new CategoryId());

        assertThrows(IllegalStateException.class, edition::closeEdition);
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionClosedEvent(edition.getId().value())
        )));
    }

    @Test
    @DisplayName("should not publish if total reading time exceeds limit")
    void shouldNotPublishIfTotalReadingTimeExceedsLimit() {
        var edition = new Edition("Edition", new EditorId(), new CategoryId());

        edition.addNote("Note 1", longContent, new AuthorId(), new EditorialId());
        edition.addNote("Note 2", longContent, new AuthorId(), new EditorialId());
        edition.addNote("Note 3", longContent, new AuthorId(), new EditorialId());
        edition.addNote("Note 4", longContent, new AuthorId(), new EditorialId());

        assertThrows(IllegalStateException.class, edition::closeEdition);

        assertEquals(Status.DRAFT, edition.getStatus());

        assertNull(edition.getPublicationDate());
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionClosedEvent(edition.getId().value())
        )));
    }

    @Test
    @DisplayName("should not publish if edition has no notes")
    void shouldNotPublishIfEditionHasNoNotes() {
        var edition = new Edition("Edition", new EditorId(), new CategoryId());

        assertThrows(IllegalStateException.class, edition::closeEdition);
        assertEquals(Status.DRAFT, edition.getStatus());
        assertNull(edition.getPublicationDate());
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionClosedEvent(edition.getId().value())
        )));
    }
}
