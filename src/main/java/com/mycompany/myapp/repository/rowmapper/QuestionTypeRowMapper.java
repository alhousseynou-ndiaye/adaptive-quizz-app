package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.QuestionType;
import com.mycompany.myapp.domain.enumeration.QuestionTypeCode;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link QuestionType}, with proper type conversions.
 */
@Service
public class QuestionTypeRowMapper implements BiFunction<Row, String, QuestionType> {

    private final ColumnConverter converter;

    public QuestionTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link QuestionType} stored in the database.
     */
    @Override
    public QuestionType apply(Row row, String prefix) {
        QuestionType entity = new QuestionType();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", QuestionTypeCode.class));
        entity.setLabel(converter.fromRow(row, prefix + "_label", String.class));
        return entity;
    }
}
