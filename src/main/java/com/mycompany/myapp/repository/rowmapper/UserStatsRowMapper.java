package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.UserStats;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserStats}, with proper type conversions.
 */
@Service
public class UserStatsRowMapper implements BiFunction<Row, String, UserStats> {

    private final ColumnConverter converter;

    public UserStatsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserStats} stored in the database.
     */
    @Override
    public UserStats apply(Row row, String prefix) {
        UserStats entity = new UserStats();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCurrentDifficulty(converter.fromRow(row, prefix + "_current_difficulty", Integer.class));
        entity.setTotalAnswered(converter.fromRow(row, prefix + "_total_answered", Integer.class));
        entity.setTotalCorrect(converter.fromRow(row, prefix + "_total_correct", Integer.class));
        entity.setStreakDays(converter.fromRow(row, prefix + "_streak_days", Integer.class));
        entity.setLastActiveAt(converter.fromRow(row, prefix + "_last_active_at", Instant.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setSubjectId(converter.fromRow(row, prefix + "_subject_id", Long.class));
        return entity;
    }
}
