package com.github.fabioper.newsletter.domain.note;

import jakarta.persistence.Embeddable;

import java.time.Duration;
import java.util.Objects;

@Embeddable
public class ReadingTime {
    private static final double WORDS_PER_SECOND = 200;

    private Duration value;

    private ReadingTime(Duration value) {
        this.value = value;
    }

    public ReadingTime() {
    }

    public static ReadingTime from(String content) {
        if (content == null) {
            return new ReadingTime(Duration.ZERO);
        }

        var wordCount = content.split("\\s+").length;
        var value = (double) wordCount / WORDS_PER_SECOND;
        var result = Duration.ofSeconds(Math.round(value * 60));
        return new ReadingTime(result);
    }

    public int getMinutes() {
        return value.toMinutesPart();
    }

    public int getSeconds() {
        return value.toSecondsPart();
    }

    @Override
    public String toString() {
        return getMinutes() + "," + getSeconds();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ReadingTime that = (ReadingTime) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
