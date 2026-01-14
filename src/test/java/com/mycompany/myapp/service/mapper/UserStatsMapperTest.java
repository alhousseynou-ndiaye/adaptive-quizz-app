package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.UserStatsAsserts.*;
import static com.mycompany.myapp.domain.UserStatsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserStatsMapperTest {

    private UserStatsMapper userStatsMapper;

    @BeforeEach
    void setUp() {
        userStatsMapper = new UserStatsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserStatsSample1();
        var actual = userStatsMapper.toEntity(userStatsMapper.toDto(expected));
        assertUserStatsAllPropertiesEquals(expected, actual);
    }
}
