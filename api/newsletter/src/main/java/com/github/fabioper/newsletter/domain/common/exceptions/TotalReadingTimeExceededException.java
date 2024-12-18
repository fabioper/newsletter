package com.github.fabioper.newsletter.domain.common.exceptions;

public class TotalReadingTimeExceededException extends IllegalStateException {
    public TotalReadingTimeExceededException(Integer limitInMinutes) {
        super("Total reading time exceeds limit of %d minutes".formatted(limitInMinutes));
    }
}
