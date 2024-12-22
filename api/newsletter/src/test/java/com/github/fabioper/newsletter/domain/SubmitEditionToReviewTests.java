package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.edition.events.EditionStatusChanges;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When submitting edition to review")
public class SubmitEditionToReviewTests {
    @Test
    @DisplayName("should update edition status")
    void shouldUpdateEditionStatus() {
        var edition = new Edition("Title", UUID.randomUUID(), UUID.randomUUID());

        edition.addNote("Note 1", shortContent, UUID.randomUUID(), UUID.randomUUID());

        edition.closeEdition();
        edition.submitToReview();

        assertEquals(Status.AVAILABLE_FOR_REVIEW, edition.getStatus());
        assertThat(edition.getDomainEvents(), hasItems(
            new EditionStatusChanges(edition.getId(), Status.CLOSED, Status.AVAILABLE_FOR_REVIEW)
        ));
    }

    @Test
    @DisplayName("should not update edition if status is different than closed")
    void shouldNotUpdateEditionIfStatusIsNotClosed() {
        var edition = new Edition("Title", UUID.randomUUID(), UUID.randomUUID());

        edition.addNote("Note 1", shortContent, UUID.randomUUID(), UUID.randomUUID());

        var originalStatus = edition.getStatus();

        assertThrows(IllegalStateException.class, edition::submitToReview);

        assertEquals(originalStatus, edition.getStatus());
    }
}
