package com.github.fabioper.newsletter.application.factories;

import com.github.fabioper.newsletter.application.dto.user.ApplicationUser;
import com.github.fabioper.newsletter.domain.author.Author;
import com.github.fabioper.newsletter.domain.editor.Editor;

public class UserRoleFactory {
    public static Author authorFrom(ApplicationUser user) {
        if (!user.isAuthor()) {
            throw new IllegalStateException("User is not an author");
        }

        return new Author(user.id());
    }

    public static Editor editorFrom(ApplicationUser user) {
        if (!user.isEditor()) {
            throw new IllegalStateException("User is not an editor");
        }

        return new Editor(user.id());
    }
}
