package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ReviewReminderAsserts.*;
import static com.mycompany.myapp.domain.ReviewReminderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewReminderMapperTest {

    private ReviewReminderMapper reviewReminderMapper;

    @BeforeEach
    void setUp() {
        reviewReminderMapper = new ReviewReminderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewReminderSample1();
        var actual = reviewReminderMapper.toEntity(reviewReminderMapper.toDto(expected));
        assertReviewReminderAllPropertiesEquals(expected, actual);
    }
}
