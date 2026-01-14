package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AnswerSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("answered_at", table, columnPrefix + "_answered_at"));
        columns.add(Column.aliased("is_correct", table, columnPrefix + "_is_correct"));
        columns.add(Column.aliased("time_spent_ms", table, columnPrefix + "_time_spent_ms"));
        columns.add(Column.aliased("self_reported_recall", table, columnPrefix + "_self_reported_recall"));
        columns.add(Column.aliased("difficulty_at_answer", table, columnPrefix + "_difficulty_at_answer"));
        columns.add(Column.aliased("selected_choice", table, columnPrefix + "_selected_choice"));
        columns.add(Column.aliased("free_text_answer", table, columnPrefix + "_free_text_answer"));

        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        columns.add(Column.aliased("question_id", table, columnPrefix + "_question_id"));
        return columns;
    }
}
