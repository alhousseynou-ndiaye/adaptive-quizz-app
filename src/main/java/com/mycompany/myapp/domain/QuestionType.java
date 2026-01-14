package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.QuestionTypeCode;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A QuestionType.
 */
@Table("question_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("code")
    private QuestionTypeCode code;

    @NotNull(message = "must not be null")
    @Size(min = 2, max = 50)
    @Column("label")
    private String label;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuestionType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionTypeCode getCode() {
        return this.code;
    }

    public QuestionType code(QuestionTypeCode code) {
        this.setCode(code);
        return this;
    }

    public void setCode(QuestionTypeCode code) {
        this.code = code;
    }

    public String getLabel() {
        return this.label;
    }

    public QuestionType label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionType)) {
            return false;
        }
        return getId() != null && getId().equals(((QuestionType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionType{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
