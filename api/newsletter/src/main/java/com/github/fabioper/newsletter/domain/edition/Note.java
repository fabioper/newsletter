package com.github.fabioper.newsletter.domain.edition;

import com.github.fabioper.newsletter.domain.shared.Guard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "notes")
public class Note {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private UUID authorId;

    @Column(nullable = false)
    private UUID editorialId;

    public Note() {
    }

    Note(String title, String content, UUID authorId, UUID editorialId) {
        Guard.againstNull(title, "title should not be null");
        Guard.againstNull(content, "content should not be null");
        Guard.againstNull(editorialId, "editorialId should not be null");
        Guard.againstNull(authorId, "authorId should not be null");

        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.editorialId = editorialId;
    }

    //region Getters
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getEditorialId() {
        return editorialId;
    }

    public ReadingTime getReadingTime() {
        return ReadingTime.from(content);
    }
    //endregion

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateEditorial(UUID editorialId) {
        this.editorialId = editorialId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Note note = (Note) object;
        return Objects.equals(getId(), note.getId());
    }
}
