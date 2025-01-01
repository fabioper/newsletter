package com.github.fabioper.newsletter.domain.note;

import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.NoteContentTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadingTimeTest {
    @Test
    void shouldCalculateReadingTimeBasedOnGivenContent() {
        assertEquals(0, ReadingTime.from(shortContent).getMinutes());
        assertEquals(27, ReadingTime.from(shortContent).getSeconds());

        assertEquals(1, ReadingTime.from(mediumContent).getMinutes());
        assertEquals(20, ReadingTime.from(mediumContent).getSeconds());

        assertEquals(3, ReadingTime.from(longContent).getMinutes());
        assertEquals(55, ReadingTime.from(longContent).getSeconds());
    }
}
