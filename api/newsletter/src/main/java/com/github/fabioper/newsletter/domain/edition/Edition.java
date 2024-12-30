package com.github.fabioper.newsletter.domain.edition;

public class Edition {
    private EditionId id;
    private String title;
    private EditionStatus status;

    public Edition() {
    }

    public Edition(String title) {
        this.title = title;
    }

    public EditionId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public EditionStatus getStatus() {
        return status;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
