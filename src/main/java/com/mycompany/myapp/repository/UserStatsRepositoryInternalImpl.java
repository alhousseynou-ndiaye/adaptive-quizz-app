package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserStats;
import com.mycompany.myapp.repository.rowmapper.SubjectRowMapper;
import com.mycompany.myapp.repository.rowmapper.UserRowMapper;
import com.mycompany.myapp.repository.rowmapper.UserStatsRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the UserStats entity.
 */
@SuppressWarnings("unused")
class UserStatsRepositoryInternalImpl extends SimpleR2dbcRepository<UserStats, Long> implements UserStatsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final SubjectRowMapper subjectMapper;
    private final UserStatsRowMapper userstatsMapper;

    private static final Table entityTable = Table.aliased("user_stats", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("jhi_user", "e_user");
    private static final Table subjectTable = Table.aliased("subject", "subject");

    public UserStatsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        SubjectRowMapper subjectMapper,
        UserStatsRowMapper userstatsMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserStats.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.subjectMapper = subjectMapper;
        this.userstatsMapper = userstatsMapper;
    }

    @Override
    public Flux<UserStats> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserStats> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserStatsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        columns.addAll(SubjectSqlHelper.getColumns(subjectTable, "subject"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable))
            .leftOuterJoin(subjectTable)
            .on(Column.create("subject_id", entityTable))
            .equals(Column.create("id", subjectTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserStats.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserStats> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserStats> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<UserStats> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<UserStats> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<UserStats> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private UserStats process(Row row, RowMetadata metadata) {
        UserStats entity = userstatsMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        entity.setSubject(subjectMapper.apply(row, "subject"));
        return entity;
    }

    @Override
    public <S extends UserStats> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
