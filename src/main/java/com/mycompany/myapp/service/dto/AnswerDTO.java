package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Answer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnswerDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant answeredAt;

    @NotNull(message = "must not be null")
    private Boolean isCorrect;

    @Min(value = 0L)
    private Long timeSpentMs;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Max(value = 2)
    private Integer selfReportedRecall;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Max(value = 5)
    private Integer difficultyAtAnswer;

    @Pattern(regexp = "^[ABCD]$")
    private String selectedChoice;

    @Lob
    private String freeTextAnswer;

    private UserDTO user;

    private QuestionDTO question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(Instant answeredAt) {
        this.answeredAt = answeredAt;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Long getTimeSpentMs() {
        return timeSpentMs;
    }

    public void setTimeSpentMs(Long timeSpentMs) {
        this.timeSpentMs = timeSpentMs;
    }

    public Integer getSelfReportedRecall() {
        return selfReportedRecall;
    }

    public void setSelfReportedRecall(Integer selfReportedRecall) {
        this.selfReportedRecall = selfReportedRecall;
    }

    public Integer getDifficultyAtAnswer() {
        return difficultyAtAnswer;
    }

    public void setDifficultyAtAnswer(Integer difficultyAtAnswer) {
        this.difficultyAtAnswer = difficultyAtAnswer;
    }

    public String getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(String selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    public String getFreeTextAnswer() {
        return freeTextAnswer;
    }

    public void setFreeTextAnswer(String freeTextAnswer) {
        this.freeTextAnswer = freeTextAnswer;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerDTO)) {
            return false;
        }

        AnswerDTO answerDTO = (AnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, answerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerDTO{" +
            "id=" + getId() +
            ", answeredAt='" + getAnsweredAt() + "'" +
            ", isCorrect='" + getIsCorrect() + "'" +
            ", timeSpentMs=" + getTimeSpentMs() +
            ", selfReportedRecall=" + getSelfReportedRecall() +
            ", difficultyAtAnswer=" + getDifficultyAtAnswer() +
            ", selectedChoice='" + getSelectedChoice() + "'" +
            ", freeTextAnswer='" + getFreeTextAnswer() + "'" +
            ", user=" + getUser() +
            ", question=" + getQuestion() +
            "}";
    }
}
