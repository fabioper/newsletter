package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When an edition review is rejected")
public class EditionRejectedTests {
    @Test
    @DisplayName("should put edition in pending adjustments status")
    void shouldUpdateStatusToPendingAdjustments() {
        var edition = new Edition("Title", UUID.randomUUID(), UUID.randomUUID());
        edition.addNote("Note1", shortContent, UUID.randomUUID(), UUID.randomUUID());

        edition.closeEdition();
        edition.submitToReview();
        edition.putUnderReview();
        edition.putAsPendingAdjustments();

        assertEquals(Status.PENDING_ADJUSTMENTS, edition.getStatus());
    }

    @Test
    @DisplayName("should not change status if it is not under review")
    void shouldNotRejectIfItIsNotUnderReview() {
        var edition = new Edition("Title", UUID.randomUUID(), UUID.randomUUID());
        edition.addNote("Note1", shortContent, UUID.randomUUID(), UUID.randomUUID());

        edition.closeEdition();
        edition.submitToReview();

        var originalStatus = edition.getStatus();
        assertThrows(IllegalStateException.class, edition::putAsPendingAdjustments);
        assertEquals(originalStatus, edition.getStatus());
    }
}
