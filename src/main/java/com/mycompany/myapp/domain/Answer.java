package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Answer.
 */
@Table("answer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("answered_at")
    private Instant answeredAt;

    @NotNull(message = "must not be null")
    @Column("is_correct")
    private Boolean isCorrect;

    @Min(value = 0L)
    @Column("time_spent_ms")
    private Long timeSpentMs;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Max(value = 2)
    @Column("self_reported_recall")
    private Integer selfReportedRecall;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Max(value = 5)
    @Column("difficulty_at_answer")
    private Integer difficultyAtAnswer;

    @Pattern(regexp = "^[ABCD]$")
    @Column("selected_choice")
    private String selectedChoice;

    @Column("free_text_answer")
    private String freeTextAnswer;

    @org.springframework.data.annotation.Transient
    private User user;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "subject", "type" }, allowSetters = true)
    private Question question;

    @Column("user_id")
    private Long userId;

    @Column("question_id")
    private Long questionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Answer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAnsweredAt() {
        return this.answeredAt;
    }

    public Answer answeredAt(Instant answeredAt) {
        this.setAnsweredAt(answeredAt);
        return this;
    }

    public void setAnsweredAt(Instant answeredAt) {
        this.answeredAt = answeredAt;
    }

    public Boolean getIsCorrect() {
        return this.isCorrect;
    }

    public Answer isCorrect(Boolean isCorrect) {
        this.setIsCorrect(isCorrect);
        return this;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Long getTimeSpentMs() {
        return this.timeSpentMs;
    }

    public Answer timeSpentMs(Long timeSpentMs) {
        this.setTimeSpentMs(timeSpentMs);
        return this;
    }

    public void setTimeSpentMs(Long timeSpentMs) {
        this.timeSpentMs = timeSpentMs;
    }

    public Integer getSelfReportedRecall() {
        return this.selfReportedRecall;
    }

    public Answer selfReportedRecall(Integer selfReportedRecall) {
        this.setSelfReportedRecall(selfReportedRecall);
        return this;
    }

    public void setSelfReportedRecall(Integer selfReportedRecall) {
        this.selfReportedRecall = selfReportedRecall;
    }

    public Integer getDifficultyAtAnswer() {
        return this.difficultyAtAnswer;
    }

    public Answer difficultyAtAnswer(Integer difficultyAtAnswer) {
        this.setDifficultyAtAnswer(difficultyAtAnswer);
        return this;
    }

    public void setDifficultyAtAnswer(Integer difficultyAtAnswer) {
        this.difficultyAtAnswer = difficultyAtAnswer;
    }

    public String getSelectedChoice() {
        return this.selectedChoice;
    }

    public Answer selectedChoice(String selectedChoice) {
        this.setSelectedChoice(selectedChoice);
        return this;
    }

    public void setSelectedChoice(String selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    public String getFreeTextAnswer() {
        return this.freeTextAnswer;
    }

    public Answer freeTextAnswer(String freeTextAnswer) {
        this.setFreeTextAnswer(freeTextAnswer);
        return this;
    }

    public void setFreeTextAnswer(String freeTextAnswer) {
        this.freeTextAnswer = freeTextAnswer;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public Answer user(User user) {
        this.setUser(user);
        return this;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
        this.questionId = question != null ? question.getId() : null;
    }

    public Answer question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(Long question) {
        this.questionId = question;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer)) {
            return false;
        }
        return getId() != null && getId().equals(((Answer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", answeredAt='" + getAnsweredAt() + "'" +
            ", isCorrect='" + getIsCorrect() + "'" +
            ", timeSpentMs=" + getTimeSpentMs() +
            ", selfReportedRecall=" + getSelfReportedRecall() +
            ", difficultyAtAnswer=" + getDifficultyAtAnswer() +
            ", selectedChoice='" + getSelectedChoice() + "'" +
            ", freeTextAnswer='" + getFreeTextAnswer() + "'" +
            "}";
    }
}
