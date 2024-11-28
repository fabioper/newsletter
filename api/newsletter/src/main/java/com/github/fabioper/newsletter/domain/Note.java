package com.github.fabioper.newsletter.domain;

import java.util.UUID;

public class Note {
    private UUID id;
    private String title;
    private String content;
    private Author author;
    private Editorial editorial;
    private ReadingTime readingTime;
    private Edition edition;

    public Note(String title, String content, Author author, Editorial editorial, Edition edition) {
        if (title == null) {
            throw new IllegalArgumentException("title should not be null");
        }

        if (content == null) {
            throw new IllegalArgumentException("content should not be null");
        }

        if (editorial == null) {
            throw new IllegalArgumentException("editorial should not be null");
        }

        if (author == null) {
            throw new IllegalArgumentException("author should not be null");
        }

        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.author = author;
        this.editorial = editorial;
        this.readingTime = ReadingTime.from(content);
        this.edition = edition;
    }

    public String getTitle() {
        return title;
    }

    public UUID getId() {
        return id;
    }

    public void updateTitle(String title) {
        if (edition != null && edition.isPublished()) {
            throw new IllegalStateException("Cannot update note assigned to a published edition");
        }

        this.title = title;
    }

    public void updateContent(String content) {
        if (edition != null && edition.isPublished()) {
            throw new IllegalStateException("Cannot update note assigned to a published edition");
        }

        this.content = content;
        this.readingTime = ReadingTime.from(content);
    }

    public void changeEditorial(Editorial editorial) {
        if (edition != null && edition.isPublished()) {
            throw new IllegalStateException("Cannot update note assigned to a published edition");
        }

        this.editorial = editorial;
    }

    public String getContent() {
        return content;
    }

    public Author getAuthor() {
        return author;
    }

    public Edition getEdition() {
        return edition;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public ReadingTime getReadingTime() {
        return readingTime;
    }

    public void updateEdition(Edition edition) {
        this.edition = edition;
    }
}
