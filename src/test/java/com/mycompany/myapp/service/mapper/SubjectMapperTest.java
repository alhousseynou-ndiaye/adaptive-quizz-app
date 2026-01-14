package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.SubjectAsserts.*;
import static com.mycompany.myapp.domain.SubjectTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubjectMapperTest {

    private SubjectMapper subjectMapper;

    @BeforeEach
    void setUp() {
        subjectMapper = new SubjectMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubjectSample1();
        var actual = subjectMapper.toEntity(subjectMapper.toDto(expected));
        assertSubjectAllPropertiesEquals(expected, actual);
    }
}
