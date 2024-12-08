package com.github.fabioper.newsletter.domain.editor;

import com.github.fabioper.newsletter.domain.category.Category;
import com.github.fabioper.newsletter.domain.collaborator.Collaborator;
import com.github.fabioper.newsletter.domain.edition.Edition;

import java.util.UUID;

public class Editor extends Collaborator {
    public Editor(UUID id) {
        super(id);
    }

    public Edition createEdition(String title, Category category) {
        return new Edition(title, this, category);
    }
}
