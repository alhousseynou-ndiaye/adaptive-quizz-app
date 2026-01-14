package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserStatsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserStats getUserStatsSample1() {
        return new UserStats().id(1L).currentDifficulty(1).totalAnswered(1).totalCorrect(1).streakDays(1);
    }

    public static UserStats getUserStatsSample2() {
        return new UserStats().id(2L).currentDifficulty(2).totalAnswered(2).totalCorrect(2).streakDays(2);
    }

    public static UserStats getUserStatsRandomSampleGenerator() {
        return new UserStats()
            .id(longCount.incrementAndGet())
            .currentDifficulty(intCount.incrementAndGet())
            .totalAnswered(intCount.incrementAndGet())
            .totalCorrect(intCount.incrementAndGet())
            .streakDays(intCount.incrementAndGet());
    }
}
