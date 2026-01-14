package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Answer;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Answer}, with proper type conversions.
 */
@Service
public class AnswerRowMapper implements BiFunction<Row, String, Answer> {

    private final ColumnConverter converter;

    public AnswerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Answer} stored in the database.
     */
    @Override
    public Answer apply(Row row, String prefix) {
        Answer entity = new Answer();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAnsweredAt(converter.fromRow(row, prefix + "_answered_at", Instant.class));
        entity.setIsCorrect(converter.fromRow(row, prefix + "_is_correct", Boolean.class));
        entity.setTimeSpentMs(converter.fromRow(row, prefix + "_time_spent_ms", Long.class));
        entity.setSelfReportedRecall(converter.fromRow(row, prefix + "_self_reported_recall", Integer.class));
        entity.setDifficultyAtAnswer(converter.fromRow(row, prefix + "_difficulty_at_answer", Integer.class));
        entity.setSelectedChoice(converter.fromRow(row, prefix + "_selected_choice", String.class));
        entity.setFreeTextAnswer(converter.fromRow(row, prefix + "_free_text_answer", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setQuestionId(converter.fromRow(row, prefix + "_question_id", Long.class));
        return entity;
    }
}
