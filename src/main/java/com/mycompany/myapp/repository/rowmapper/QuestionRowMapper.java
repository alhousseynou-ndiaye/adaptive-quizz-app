package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Question;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Question}, with proper type conversions.
 */
@Service
public class QuestionRowMapper implements BiFunction<Row, String, Question> {

    private final ColumnConverter converter;

    public QuestionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Question} stored in the database.
     */
    @Override
    public Question apply(Row row, String prefix) {
        Question entity = new Question();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPrompt(converter.fromRow(row, prefix + "_prompt", String.class));
        entity.setDifficulty(converter.fromRow(row, prefix + "_difficulty", Integer.class));
        entity.setExplanation(converter.fromRow(row, prefix + "_explanation", String.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        entity.setChoiceA(converter.fromRow(row, prefix + "_choice_a", String.class));
        entity.setChoiceB(converter.fromRow(row, prefix + "_choice_b", String.class));
        entity.setChoiceC(converter.fromRow(row, prefix + "_choice_c", String.class));
        entity.setChoiceD(converter.fromRow(row, prefix + "_choice_d", String.class));
        entity.setCorrectChoice(converter.fromRow(row, prefix + "_correct_choice", String.class));
        entity.setCorrectText(converter.fromRow(row, prefix + "_correct_text", String.class));
        entity.setSubjectId(converter.fromRow(row, prefix + "_subject_id", Long.class));
        entity.setTypeId(converter.fromRow(row, prefix + "_type_id", Long.class));
        return entity;
    }
}
