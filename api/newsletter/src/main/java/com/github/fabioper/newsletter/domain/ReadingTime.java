package com.github.fabioper.newsletter.domain;

public record ReadingTime(int minutes) {
    public static ReadingTime calculate(String content) {
        return new ReadingTime(0);
    }
}
