package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.edition.events.EditionPublishedEvent;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.note.Note;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.longContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When publishing edition")
public class WhenPublishingEditionTests {
    @Test
    @DisplayName("should update status, publication date and lock notes for changes")
    void shouldUpdateStatusAndPublicationDateAndLockNotes() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");

        edition.assignNote(new Note("Note 1", longContent, UUID.randomUUID(), editorial));
        edition.assignNote(new Note("Note 2", longContent, UUID.randomUUID(), editorial));

        edition.publish();

        assertEquals(Status.PUBLISHED, edition.getStatus());
        assertNotNull(edition.getPublicationDate());

        edition.getNotes().forEach(note -> assertTrue(note.isLockedForChanges()));

        assertThat(edition.getDomainEvents(), hasItems(
            new EditionPublishedEvent(edition.getId())
        ));
    }

    @Test
    @DisplayName("should not publish if edition was already published")
    void shouldNotPublishIfEditionAlreadyPublished() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        assertThrows(IllegalStateException.class, edition::publish);
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionPublishedEvent(edition.getId())
        )));
    }

    @Test
    @DisplayName("should not publish if total reading time exceeds limit")
    void shouldNotPublishIfTotalReadingTimeExceedsLimit() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");

        edition.updateNotes(List.of(
            new Note("Note 1", longContent, UUID.randomUUID(), editorial),
            new Note("Note 2", longContent, UUID.randomUUID(), editorial),
            new Note("Note 3", longContent, UUID.randomUUID(), editorial),
            new Note("Note 4", longContent, UUID.randomUUID(), editorial)
        ));

        assertThrows(IllegalStateException.class, edition::publish);
        assertEquals(Status.DRAFT, edition.getStatus());
        assertNull(edition.getPublicationDate());
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionPublishedEvent(edition.getId())
        )));

        edition.getNotes().forEach(note -> assertFalse(note.isLockedForChanges()));
    }

    @Test
    @DisplayName("should not publish if edition has no notes")
    void shouldNotPublishIfEditionHasNoNotes() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        assertThrows(IllegalStateException.class, edition::publish);
        assertEquals(Status.DRAFT, edition.getStatus());
        assertNull(edition.getPublicationDate());
        assertThat(edition.getDomainEvents(), not(hasItems(
            new EditionPublishedEvent(edition.getId())
        )));

        edition.getNotes().forEach(note -> assertFalse(note.isLockedForChanges()));
    }
}
