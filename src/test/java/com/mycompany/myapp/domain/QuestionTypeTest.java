package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuestionTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionType.class);
        QuestionType questionType1 = getQuestionTypeSample1();
        QuestionType questionType2 = new QuestionType();
        assertThat(questionType1).isNotEqualTo(questionType2);

        questionType2.setId(questionType1.getId());
        assertThat(questionType1).isEqualTo(questionType2);

        questionType2 = getQuestionTypeSample2();
        assertThat(questionType1).isNotEqualTo(questionType2);
    }
}
