package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserStatsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserStatsDTO.class);
        UserStatsDTO userStatsDTO1 = new UserStatsDTO();
        userStatsDTO1.setId(1L);
        UserStatsDTO userStatsDTO2 = new UserStatsDTO();
        assertThat(userStatsDTO1).isNotEqualTo(userStatsDTO2);
        userStatsDTO2.setId(userStatsDTO1.getId());
        assertThat(userStatsDTO1).isEqualTo(userStatsDTO2);
        userStatsDTO2.setId(2L);
        assertThat(userStatsDTO1).isNotEqualTo(userStatsDTO2);
        userStatsDTO1.setId(null);
        assertThat(userStatsDTO1).isNotEqualTo(userStatsDTO2);
    }
}
