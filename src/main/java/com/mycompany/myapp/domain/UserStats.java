package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserStats.
 */
@Table("user_stats")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Max(value = 5)
    @Column("current_difficulty")
    private Integer currentDifficulty;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("total_answered")
    private Integer totalAnswered;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("total_correct")
    private Integer totalCorrect;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("streak_days")
    private Integer streakDays;

    @Column("last_active_at")
    private Instant lastActiveAt;

    @org.springframework.data.annotation.Transient
    private User user;

    @org.springframework.data.annotation.Transient
    private Subject subject;

    @Column("user_id")
    private Long userId;

    @Column("subject_id")
    private Long subjectId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserStats id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCurrentDifficulty() {
        return this.currentDifficulty;
    }

    public UserStats currentDifficulty(Integer currentDifficulty) {
        this.setCurrentDifficulty(currentDifficulty);
        return this;
    }

    public void setCurrentDifficulty(Integer currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }

    public Integer getTotalAnswered() {
        return this.totalAnswered;
    }

    public UserStats totalAnswered(Integer totalAnswered) {
        this.setTotalAnswered(totalAnswered);
        return this;
    }

    public void setTotalAnswered(Integer totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public Integer getTotalCorrect() {
        return this.totalCorrect;
    }

    public UserStats totalCorrect(Integer totalCorrect) {
        this.setTotalCorrect(totalCorrect);
        return this;
    }

    public void setTotalCorrect(Integer totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public Integer getStreakDays() {
        return this.streakDays;
    }

    public UserStats streakDays(Integer streakDays) {
        this.setStreakDays(streakDays);
        return this;
    }

    public void setStreakDays(Integer streakDays) {
        this.streakDays = streakDays;
    }

    public Instant getLastActiveAt() {
        return this.lastActiveAt;
    }

    public UserStats lastActiveAt(Instant lastActiveAt) {
        this.setLastActiveAt(lastActiveAt);
        return this;
    }

    public void setLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public UserStats user(User user) {
        this.setUser(user);
        return this;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        this.subjectId = subject != null ? subject.getId() : null;
    }

    public UserStats subject(Subject subject) {
        this.setSubject(subject);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    public Long getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Long subject) {
        this.subjectId = subject;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserStats)) {
            return false;
        }
        return getId() != null && getId().equals(((UserStats) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserStats{" +
            "id=" + getId() +
            ", currentDifficulty=" + getCurrentDifficulty() +
            ", totalAnswered=" + getTotalAnswered() +
            ", totalCorrect=" + getTotalCorrect() +
            ", streakDays=" + getStreakDays() +
            ", lastActiveAt='" + getLastActiveAt() + "'" +
            "}";
    }
}
