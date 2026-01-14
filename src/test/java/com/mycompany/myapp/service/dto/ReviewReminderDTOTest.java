package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewReminderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewReminderDTO.class);
        ReviewReminderDTO reviewReminderDTO1 = new ReviewReminderDTO();
        reviewReminderDTO1.setId(1L);
        ReviewReminderDTO reviewReminderDTO2 = new ReviewReminderDTO();
        assertThat(reviewReminderDTO1).isNotEqualTo(reviewReminderDTO2);
        reviewReminderDTO2.setId(reviewReminderDTO1.getId());
        assertThat(reviewReminderDTO1).isEqualTo(reviewReminderDTO2);
        reviewReminderDTO2.setId(2L);
        assertThat(reviewReminderDTO1).isNotEqualTo(reviewReminderDTO2);
        reviewReminderDTO1.setId(null);
        assertThat(reviewReminderDTO1).isNotEqualTo(reviewReminderDTO2);
    }
}
