package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.ReminderKind;
import com.mycompany.myapp.domain.enumeration.ReminderStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ReviewReminder.
 */
@Table("review_reminder")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReviewReminder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("due_at")
    private Instant dueAt;

    @NotNull(message = "must not be null")
    @Column("status")
    private ReminderStatus status;

    @NotNull(message = "must not be null")
    @Column("kind")
    private ReminderKind kind;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private Instant createdAt;

    @Column("done_at")
    private Instant doneAt;

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

    public ReviewReminder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDueAt() {
        return this.dueAt;
    }

    public ReviewReminder dueAt(Instant dueAt) {
        this.setDueAt(dueAt);
        return this;
    }

    public void setDueAt(Instant dueAt) {
        this.dueAt = dueAt;
    }

    public ReminderStatus getStatus() {
        return this.status;
    }

    public ReviewReminder status(ReminderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReminderStatus status) {
        this.status = status;
    }

    public ReminderKind getKind() {
        return this.kind;
    }

    public ReviewReminder kind(ReminderKind kind) {
        this.setKind(kind);
        return this;
    }

    public void setKind(ReminderKind kind) {
        this.kind = kind;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ReviewReminder createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDoneAt() {
        return this.doneAt;
    }

    public ReviewReminder doneAt(Instant doneAt) {
        this.setDoneAt(doneAt);
        return this;
    }

    public void setDoneAt(Instant doneAt) {
        this.doneAt = doneAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public ReviewReminder user(User user) {
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

    public ReviewReminder question(Question question) {
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
        if (!(o instanceof ReviewReminder)) {
            return false;
        }
        return getId() != null && getId().equals(((ReviewReminder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReviewReminder{" +
            "id=" + getId() +
            ", dueAt='" + getDueAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", kind='" + getKind() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", doneAt='" + getDoneAt() + "'" +
            "}";
    }
}
