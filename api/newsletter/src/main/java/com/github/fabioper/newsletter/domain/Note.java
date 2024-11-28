package com.github.fabioper.newsletter.domain;

import java.util.UUID;

public class Note {
    private UUID id;
    private String title;
    private String content;
    private Author author;
    private Editorial editorial;
    private ReadingTime readingTime;

    public Note(String title, String content, Author author, Editorial editorial) {
        if (title == null)
            throw new IllegalArgumentException("title should not be null");

        if (content == null)
            throw new IllegalArgumentException("content should not be null");

        if (editorial == null)
            throw new IllegalArgumentException("editorial should not be null");

        if (author == null)
            throw new IllegalArgumentException("author should not be null");

        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.author = author;
        this.editorial = editorial;
        this.readingTime = ReadingTime.calculate(content);
    }

    public String getTitle() {
        return title;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Author getAuthor() {
        return author;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public ReadingTime getReadingTime() {
        return readingTime;
    }
}
