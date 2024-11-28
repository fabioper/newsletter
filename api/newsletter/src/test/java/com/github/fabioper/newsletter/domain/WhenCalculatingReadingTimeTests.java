package com.github.fabioper.newsletter.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.fabioper.newsletter.testdata.ReadingTimeTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("When calculating reading time")
class WhenCalculatingReadingTimeTests {

    @Test
    @DisplayName("should get the correct number of minutes based on content")
    void shouldGetCorrectNumberOfMinutesBasedOnContent() {
        assertEquals(4, ReadingTime.calculate(longContent).minutes());
        assertEquals(1, ReadingTime.calculate(shortContent).minutes());
        assertEquals(2, ReadingTime.calculate(mediumContent).minutes());
    }
}
