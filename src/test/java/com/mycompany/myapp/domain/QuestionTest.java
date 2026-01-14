package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuestionTestSamples.*;
import static com.mycompany.myapp.domain.QuestionTypeTestSamples.*;
import static com.mycompany.myapp.domain.SubjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void subjectTest() {
        Question question = getQuestionRandomSampleGenerator();
        Subject subjectBack = getSubjectRandomSampleGenerator();

        question.setSubject(subjectBack);
        assertThat(question.getSubject()).isEqualTo(subjectBack);

        question.subject(null);
        assertThat(question.getSubject()).isNull();
    }

    @Test
    void typeTest() {
        Question question = getQuestionRandomSampleGenerator();
        QuestionType questionTypeBack = getQuestionTypeRandomSampleGenerator();

        question.setType(questionTypeBack);
        assertThat(question.getType()).isEqualTo(questionTypeBack);

        question.type(null);
        assertThat(question.getType()).isNull();
    }
}
