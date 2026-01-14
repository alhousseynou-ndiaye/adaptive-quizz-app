package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ReviewReminder;
import com.mycompany.myapp.domain.enumeration.ReminderKind;
import com.mycompany.myapp.domain.enumeration.ReminderStatus;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReviewReminder}, with proper type conversions.
 */
@Service
public class ReviewReminderRowMapper implements BiFunction<Row, String, ReviewReminder> {

    private final ColumnConverter converter;

    public ReviewReminderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReviewReminder} stored in the database.
     */
    @Override
    public ReviewReminder apply(Row row, String prefix) {
        ReviewReminder entity = new ReviewReminder();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDueAt(converter.fromRow(row, prefix + "_due_at", Instant.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", ReminderStatus.class));
        entity.setKind(converter.fromRow(row, prefix + "_kind", ReminderKind.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setDoneAt(converter.fromRow(row, prefix + "_done_at", Instant.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setQuestionId(converter.fromRow(row, prefix + "_question_id", Long.class));
        return entity;
    }
}
