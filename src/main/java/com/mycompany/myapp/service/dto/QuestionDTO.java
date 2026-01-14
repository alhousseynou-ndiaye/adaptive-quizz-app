package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Question} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionDTO implements Serializable {

    private Long id;

    @Lob
    private String prompt;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Max(value = 5)
    private Integer difficulty;

    @Lob
    private String explanation;

    @NotNull(message = "must not be null")
    private Boolean active;

    @Size(max = 500)
    private String choiceA;

    @Size(max = 500)
    private String choiceB;

    @Size(max = 500)
    private String choiceC;

    @Size(max = 500)
    private String choiceD;

    @Pattern(regexp = "^[ABCD]$")
    private String correctChoice;

    @Lob
    private String correctText;

    private SubjectDTO subject;

    private QuestionTypeDTO type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getCorrectChoice() {
        return correctChoice;
    }

    public void setCorrectChoice(String correctChoice) {
        this.correctChoice = correctChoice;
    }

    public String getCorrectText() {
        return correctText;
    }

    public void setCorrectText(String correctText) {
        this.correctText = correctText;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public QuestionTypeDTO getType() {
        return type;
    }

    public void setType(QuestionTypeDTO type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionDTO)) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", prompt='" + getPrompt() + "'" +
            ", difficulty=" + getDifficulty() +
            ", explanation='" + getExplanation() + "'" +
            ", active='" + getActive() + "'" +
            ", choiceA='" + getChoiceA() + "'" +
            ", choiceB='" + getChoiceB() + "'" +
            ", choiceC='" + getChoiceC() + "'" +
            ", choiceD='" + getChoiceD() + "'" +
            ", correctChoice='" + getCorrectChoice() + "'" +
            ", correctText='" + getCorrectText() + "'" +
            ", subject=" + getSubject() +
            ", type=" + getType() +
            "}";
    }
}
