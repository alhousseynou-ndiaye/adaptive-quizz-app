package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.ReminderKind;
import com.mycompany.myapp.domain.enumeration.ReminderStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ReviewReminder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReviewReminderDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant dueAt;

    @NotNull(message = "must not be null")
    private ReminderStatus status;

    @NotNull(message = "must not be null")
    private ReminderKind kind;

    @NotNull(message = "must not be null")
    private Instant createdAt;

    private Instant doneAt;

    private UserDTO user;

    private QuestionDTO question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public void setDueAt(Instant dueAt) {
        this.dueAt = dueAt;
    }

    public ReminderStatus getStatus() {
        return status;
    }

    public void setStatus(ReminderStatus status) {
        this.status = status;
    }

    public ReminderKind getKind() {
        return kind;
    }

    public void setKind(ReminderKind kind) {
        this.kind = kind;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(Instant doneAt) {
        this.doneAt = doneAt;
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
        if (!(o instanceof ReviewReminderDTO)) {
            return false;
        }

        ReviewReminderDTO reviewReminderDTO = (ReviewReminderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reviewReminderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReviewReminderDTO{" +
            "id=" + getId() +
            ", dueAt='" + getDueAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", kind='" + getKind() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", doneAt='" + getDoneAt() + "'" +
            ", user=" + getUser() +
            ", question=" + getQuestion() +
            "}";
    }
}
