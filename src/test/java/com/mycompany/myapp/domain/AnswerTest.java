package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AnswerTestSamples.*;
import static com.mycompany.myapp.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Answer.class);
        Answer answer1 = getAnswerSample1();
        Answer answer2 = new Answer();
        assertThat(answer1).isNotEqualTo(answer2);

        answer2.setId(answer1.getId());
        assertThat(answer1).isEqualTo(answer2);

        answer2 = getAnswerSample2();
        assertThat(answer1).isNotEqualTo(answer2);
    }

    @Test
    void questionTest() {
        Answer answer = getAnswerRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        answer.setQuestion(questionBack);
        assertThat(answer.getQuestion()).isEqualTo(questionBack);

        answer.question(null);
        assertThat(answer.getQuestion()).isNull();
    }
}
