package com.github.fabioper.newsletter.domain.edition;

import java.util.Arrays;

public record ReadingTime(int minutes) {
    private static final float WORDS_PER_MINUTE = 200f;

    public static ReadingTime from(String content) {
        var words = Arrays.stream(content.split(" ")).toList();
        var totalMinutes = Math.ceil(words.size() / ReadingTime.WORDS_PER_MINUTE);

        return new ReadingTime((int) totalMinutes);
    }
}
