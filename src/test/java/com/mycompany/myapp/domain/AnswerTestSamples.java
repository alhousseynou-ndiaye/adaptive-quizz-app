package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AnswerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Answer getAnswerSample1() {
        return new Answer().id(1L).timeSpentMs(1L).selfReportedRecall(1).difficultyAtAnswer(1).selectedChoice("selectedChoice1");
    }

    public static Answer getAnswerSample2() {
        return new Answer().id(2L).timeSpentMs(2L).selfReportedRecall(2).difficultyAtAnswer(2).selectedChoice("selectedChoice2");
    }

    public static Answer getAnswerRandomSampleGenerator() {
        return new Answer()
            .id(longCount.incrementAndGet())
            .timeSpentMs(longCount.incrementAndGet())
            .selfReportedRecall(intCount.incrementAndGet())
            .difficultyAtAnswer(intCount.incrementAndGet())
            .selectedChoice(UUID.randomUUID().toString());
    }
}
