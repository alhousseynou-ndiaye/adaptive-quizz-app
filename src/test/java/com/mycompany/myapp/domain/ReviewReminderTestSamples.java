package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ReviewReminderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReviewReminder getReviewReminderSample1() {
        return new ReviewReminder().id(1L);
    }

    public static ReviewReminder getReviewReminderSample2() {
        return new ReviewReminder().id(2L);
    }

    public static ReviewReminder getReviewReminderRandomSampleGenerator() {
        return new ReviewReminder().id(longCount.incrementAndGet());
    }
}
