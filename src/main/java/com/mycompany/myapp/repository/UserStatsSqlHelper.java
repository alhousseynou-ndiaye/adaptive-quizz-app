package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserStatsSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("current_difficulty", table, columnPrefix + "_current_difficulty"));
        columns.add(Column.aliased("total_answered", table, columnPrefix + "_total_answered"));
        columns.add(Column.aliased("total_correct", table, columnPrefix + "_total_correct"));
        columns.add(Column.aliased("streak_days", table, columnPrefix + "_streak_days"));
        columns.add(Column.aliased("last_active_at", table, columnPrefix + "_last_active_at"));

        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        columns.add(Column.aliased("subject_id", table, columnPrefix + "_subject_id"));
        return columns;
    }
}
