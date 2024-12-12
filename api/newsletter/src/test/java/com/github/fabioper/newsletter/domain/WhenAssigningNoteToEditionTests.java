package com.github.fabioper.newsletter.domain;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.edition.Edition;
import com.github.fabioper.newsletter.domain.edition.events.NoteAssignedToEditionEvent;
import com.github.fabioper.newsletter.domain.editorial.Editorial;
import com.github.fabioper.newsletter.domain.note.Note;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.shortContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("When assigning note to edition")
public class WhenAssigningNoteToEditionTests {
    @Test
    @DisplayName("should assign note to edition")
    void shouldAssignNoteToEdition() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));
        var editorial = new Editorial("Editorial");

        var note = new Note("Title", shortContent, UUID.randomUUID(), editorial);
        edition.assignNote(note);

        assertThat(edition.getNotes(), hasItems(note));
        assertThat(edition.getDomainEvents(), hasItems(new NoteAssignedToEditionEvent(note.getId(), edition.getId())));
    }

    @Test
    @DisplayName("should not assign if edition is published")
    void shouldNotAssignIfEditionIsPublished() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));
        var editorial = new Editorial("Editorial");

        edition.assignNote(new Note("Title", "Content", UUID.randomUUID(), editorial));
        edition.assignNote(new Note("Title", "Content", UUID.randomUUID(), editorial));

        edition.publish();

        var note = new Note("Title", "Content", UUID.randomUUID(), editorial);

        assertThrows(IllegalStateException.class, () -> edition.assignNote(note));

        assertThat(
            edition.getDomainEvents(),
            not(hasItems(new NoteAssignedToEditionEvent(note.getId(), edition.getId())))
        );
    }

    @Test
    @DisplayName("should not assign if note is already assigned to edition")
    void shouldThrowIfNoteIsAlreadyAssigned() {
        var edition = new Edition("Edition", UUID.randomUUID(), new Category("Category"));

        var editorial = new Editorial("Editorial");
        var note = new Note("Title", "Content", UUID.randomUUID(), editorial);
        edition.assignNote(note);

        edition.clearEvents();

        assertThrows(IllegalArgumentException.class, () -> edition.assignNote(note));

        assertThat(
            edition.getDomainEvents(),
            not(hasItems(new NoteAssignedToEditionEvent(note.getId(), edition.getId())))
        );
    }
}
