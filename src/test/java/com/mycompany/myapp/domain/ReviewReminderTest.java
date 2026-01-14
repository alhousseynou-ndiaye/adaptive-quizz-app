package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuestionTestSamples.*;
import static com.mycompany.myapp.domain.ReviewReminderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewReminderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewReminder.class);
        ReviewReminder reviewReminder1 = getReviewReminderSample1();
        ReviewReminder reviewReminder2 = new ReviewReminder();
        assertThat(reviewReminder1).isNotEqualTo(reviewReminder2);

        reviewReminder2.setId(reviewReminder1.getId());
        assertThat(reviewReminder1).isEqualTo(reviewReminder2);

        reviewReminder2 = getReviewReminderSample2();
        assertThat(reviewReminder1).isNotEqualTo(reviewReminder2);
    }

    @Test
    void questionTest() {
        ReviewReminder reviewReminder = getReviewReminderRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        reviewReminder.setQuestion(questionBack);
        assertThat(reviewReminder.getQuestion()).isEqualTo(questionBack);

        reviewReminder.question(null);
        assertThat(reviewReminder.getQuestion()).isNull();
    }
}
