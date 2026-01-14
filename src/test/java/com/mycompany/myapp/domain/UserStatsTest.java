package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SubjectTestSamples.*;
import static com.mycompany.myapp.domain.UserStatsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserStatsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserStats.class);
        UserStats userStats1 = getUserStatsSample1();
        UserStats userStats2 = new UserStats();
        assertThat(userStats1).isNotEqualTo(userStats2);

        userStats2.setId(userStats1.getId());
        assertThat(userStats1).isEqualTo(userStats2);

        userStats2 = getUserStatsSample2();
        assertThat(userStats1).isNotEqualTo(userStats2);
    }

    @Test
    void subjectTest() {
        UserStats userStats = getUserStatsRandomSampleGenerator();
        Subject subjectBack = getSubjectRandomSampleGenerator();

        userStats.setSubject(subjectBack);
        assertThat(userStats.getSubject()).isEqualTo(subjectBack);

        userStats.subject(null);
        assertThat(userStats.getSubject()).isNull();
    }
}
