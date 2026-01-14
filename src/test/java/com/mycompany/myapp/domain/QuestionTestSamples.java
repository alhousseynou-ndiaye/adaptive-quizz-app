package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Question getQuestionSample1() {
        return new Question()
            .id(1L)
            .difficulty(1)
            .choiceA("choiceA1")
            .choiceB("choiceB1")
            .choiceC("choiceC1")
            .choiceD("choiceD1")
            .correctChoice("correctChoice1");
    }

    public static Question getQuestionSample2() {
        return new Question()
            .id(2L)
            .difficulty(2)
            .choiceA("choiceA2")
            .choiceB("choiceB2")
            .choiceC("choiceC2")
            .choiceD("choiceD2")
            .correctChoice("correctChoice2");
    }

    public static Question getQuestionRandomSampleGenerator() {
        return new Question()
            .id(longCount.incrementAndGet())
            .difficulty(intCount.incrementAndGet())
            .choiceA(UUID.randomUUID().toString())
            .choiceB(UUID.randomUUID().toString())
            .choiceC(UUID.randomUUID().toString())
            .choiceD(UUID.randomUUID().toString())
            .correctChoice(UUID.randomUUID().toString());
    }
}
