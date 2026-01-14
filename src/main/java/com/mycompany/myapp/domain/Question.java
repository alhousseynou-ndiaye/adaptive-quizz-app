package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Question.
 */
@Table("question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("prompt")
    private String prompt;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Max(value = 5)
    @Column("difficulty")
    private Integer difficulty;

    @Column("explanation")
    private String explanation;

    @NotNull(message = "must not be null")
    @Column("active")
    private Boolean active;

    @Size(max = 500)
    @Column("choice_a")
    private String choiceA;

    @Size(max = 500)
    @Column("choice_b")
    private String choiceB;

    @Size(max = 500)
    @Column("choice_c")
    private String choiceC;

    @Size(max = 500)
    @Column("choice_d")
    private String choiceD;

    @Pattern(regexp = "^[ABCD]$")
    @Column("correct_choice")
    private String correctChoice;

    @Column("correct_text")
    private String correctText;

    @org.springframework.data.annotation.Transient
    private Subject subject;

    @org.springframework.data.annotation.Transient
    private QuestionType type;

    @Column("subject_id")
    private Long subjectId;

    @Column("type_id")
    private Long typeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public Question prompt(String prompt) {
        this.setPrompt(prompt);
        return this;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getDifficulty() {
        return this.difficulty;
    }

    public Question difficulty(Integer difficulty) {
        this.setDifficulty(difficulty);
        return this;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public Question explanation(String explanation) {
        this.setExplanation(explanation);
        return this;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Question active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getChoiceA() {
        return this.choiceA;
    }

    public Question choiceA(String choiceA) {
        this.setChoiceA(choiceA);
        return this;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return this.choiceB;
    }

    public Question choiceB(String choiceB) {
        this.setChoiceB(choiceB);
        return this;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return this.choiceC;
    }

    public Question choiceC(String choiceC) {
        this.setChoiceC(choiceC);
        return this;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return this.choiceD;
    }

    public Question choiceD(String choiceD) {
        this.setChoiceD(choiceD);
        return this;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getCorrectChoice() {
        return this.correctChoice;
    }

    public Question correctChoice(String correctChoice) {
        this.setCorrectChoice(correctChoice);
        return this;
    }

    public void setCorrectChoice(String correctChoice) {
        this.correctChoice = correctChoice;
    }

    public String getCorrectText() {
        return this.correctText;
    }

    public Question correctText(String correctText) {
        this.setCorrectText(correctText);
        return this;
    }

    public void setCorrectText(String correctText) {
        this.correctText = correctText;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        this.subjectId = subject != null ? subject.getId() : null;
    }

    public Question subject(Subject subject) {
        this.setSubject(subject);
        return this;
    }

    public QuestionType getType() {
        return this.type;
    }

    public void setType(QuestionType questionType) {
        this.type = questionType;
        this.typeId = questionType != null ? questionType.getId() : null;
    }

    public Question type(QuestionType questionType) {
        this.setType(questionType);
        return this;
    }

    public Long getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Long subject) {
        this.subjectId = subject;
    }

    public Long getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Long questionType) {
        this.typeId = questionType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
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
            "}";
    }
}
