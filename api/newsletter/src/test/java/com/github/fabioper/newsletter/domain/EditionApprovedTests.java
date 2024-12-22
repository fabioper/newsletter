package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When an edition review is approved")
public class EditionApprovedTests {
    @Test
    @DisplayName("should update status to approved")
    void shouldUpdateStatusToApproved() {
        var edition = new Edition("Title", UUID.randomUUID(), UUID.randomUUID());
        edition.addNote("Note1", shortContent, UUID.randomUUID(), UUID.randomUUID());

        edition.closeEdition();
        edition.submitToReview();
        edition.putUnderReview();
        edition.approve();

        assertEquals(Status.APPROVED, edition.getStatus());
    }

    @Test
    @DisplayName("should update status if it was not previously under review")
    void shouldNotApproveIfItIsNotUnderReview() {
        var edition = new Edition("Title", UUID.randomUUID(), UUID.randomUUID());
        edition.addNote("Note1", shortContent, UUID.randomUUID(), UUID.randomUUID());

        edition.closeEdition();
        edition.submitToReview();

        var originalStatus = edition.getStatus();
        assertThrows(IllegalStateException.class, edition::approve);
        assertEquals(originalStatus, edition.getStatus());
    }
}
