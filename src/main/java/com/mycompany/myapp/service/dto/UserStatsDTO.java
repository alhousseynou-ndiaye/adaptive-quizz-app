package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.UserStats} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserStatsDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Max(value = 5)
    private Integer currentDifficulty;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer totalAnswered;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer totalCorrect;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer streakDays;

    private Instant lastActiveAt;

    private UserDTO user;

    private SubjectDTO subject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(Integer currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }

    public Integer getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(Integer totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public Integer getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(Integer totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public Integer getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(Integer streakDays) {
        this.streakDays = streakDays;
    }

    public Instant getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserStatsDTO)) {
            return false;
        }

        UserStatsDTO userStatsDTO = (UserStatsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userStatsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserStatsDTO{" +
            "id=" + getId() +
            ", currentDifficulty=" + getCurrentDifficulty() +
            ", totalAnswered=" + getTotalAnswered() +
            ", totalCorrect=" + getTotalCorrect() +
            ", streakDays=" + getStreakDays() +
            ", lastActiveAt='" + getLastActiveAt() + "'" +
            ", user=" + getUser() +
            ", subject=" + getSubject() +
            "}";
    }
}
