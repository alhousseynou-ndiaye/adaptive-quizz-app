package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QuestionType getQuestionTypeSample1() {
        return new QuestionType().id(1L).label("label1");
    }

    public static QuestionType getQuestionTypeSample2() {
        return new QuestionType().id(2L).label("label2");
    }

    public static QuestionType getQuestionTypeRandomSampleGenerator() {
        return new QuestionType().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString());
    }
}
