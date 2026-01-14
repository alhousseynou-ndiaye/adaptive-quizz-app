package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class QuestionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("prompt", table, columnPrefix + "_prompt"));
        columns.add(Column.aliased("difficulty", table, columnPrefix + "_difficulty"));
        columns.add(Column.aliased("explanation", table, columnPrefix + "_explanation"));
        columns.add(Column.aliased("active", table, columnPrefix + "_active"));
        columns.add(Column.aliased("choice_a", table, columnPrefix + "_choice_a"));
        columns.add(Column.aliased("choice_b", table, columnPrefix + "_choice_b"));
        columns.add(Column.aliased("choice_c", table, columnPrefix + "_choice_c"));
        columns.add(Column.aliased("choice_d", table, columnPrefix + "_choice_d"));
        columns.add(Column.aliased("correct_choice", table, columnPrefix + "_correct_choice"));
        columns.add(Column.aliased("correct_text", table, columnPrefix + "_correct_text"));

        columns.add(Column.aliased("subject_id", table, columnPrefix + "_subject_id"));
        columns.add(Column.aliased("type_id", table, columnPrefix + "_type_id"));
        return columns;
    }
}
