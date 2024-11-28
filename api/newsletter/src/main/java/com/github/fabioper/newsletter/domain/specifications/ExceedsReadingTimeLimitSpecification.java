package com.github.fabioper.newsletter.domain.specifications;

import com.github.fabioper.newsletter.domain.Edition;

public class ExceedsReadingTimeLimitSpecification implements Specification<Edition> {
    public static final int READING_TIME_LIMIT_IN_MINUTES = 8;

    @Override
    public boolean isSatisfiedBy(Edition edition) {
        return getTotalReadingTime(edition) > READING_TIME_LIMIT_IN_MINUTES;
    }

    private int getTotalReadingTime(Edition candidate) {
        return candidate.getNotes().stream().mapToInt(note -> note.getReadingTime().minutes()).sum();
    }
}
