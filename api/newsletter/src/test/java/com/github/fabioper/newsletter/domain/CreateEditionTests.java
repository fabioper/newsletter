package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.Status;
import com.github.fabioper.newsletter.domain.edition.events.EditionCreatedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("When creating edition")
class CreateEditionTests {

    @Test
    @DisplayName("should create as a draft")
    void shouldCreateEditionAsADraft() {
        var editorId = UUID.randomUUID();
        var categoryId = UUID.randomUUID();
        var edition = new Edition("Edition", editorId, categoryId);

        assertNotNull(edition.getId());
        assertEquals("Edition", edition.getTitle());
        assertEquals(Status.DRAFT, edition.getStatus());
        assertEquals(categoryId, edition.getCategoryId());
        assertEquals(editorId, edition.getEditorId());
        assertNull(edition.getPublicationDate());

        assertThat(edition.getDomainEvents(), hasItems(
            new EditionCreatedEvent(edition.getId())
        ));
    }
}
