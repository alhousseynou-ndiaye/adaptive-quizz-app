package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.QuestionTypeCode;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.QuestionType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionTypeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private QuestionTypeCode code;

    @NotNull(message = "must not be null")
    @Size(min = 2, max = 50)
    private String label;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionTypeCode getCode() {
        return code;
    }

    public void setCode(QuestionTypeCode code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionTypeDTO)) {
            return false;
        }

        QuestionTypeDTO questionTypeDTO = (QuestionTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionTypeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
