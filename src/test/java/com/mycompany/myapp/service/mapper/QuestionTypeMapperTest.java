package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.QuestionTypeAsserts.*;
import static com.mycompany.myapp.domain.QuestionTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionTypeMapperTest {

    private QuestionTypeMapper questionTypeMapper;

    @BeforeEach
    void setUp() {
        questionTypeMapper = new QuestionTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuestionTypeSample1();
        var actual = questionTypeMapper.toEntity(questionTypeMapper.toDto(expected));
        assertQuestionTypeAllPropertiesEquals(expected, actual);
    }
}
