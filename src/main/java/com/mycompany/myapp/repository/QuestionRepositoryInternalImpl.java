package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.repository.rowmapper.QuestionRowMapper;
import com.mycompany.myapp.repository.rowmapper.QuestionTypeRowMapper;
import com.mycompany.myapp.repository.rowmapper.SubjectRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Question entity.
 */
@SuppressWarnings("unused")
class QuestionRepositoryInternalImpl extends SimpleR2dbcRepository<Question, Long> implements QuestionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SubjectRowMapper subjectMapper;
    private final QuestionTypeRowMapper questiontypeMapper;
    private final QuestionRowMapper questionMapper;

    private static final Table entityTable = Table.aliased("question", EntityManager.ENTITY_ALIAS);
    private static final Table subjectTable = Table.aliased("subject", "subject");
    private static final Table typeTable = Table.aliased("question_type", "e_type");

    public QuestionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SubjectRowMapper subjectMapper,
        QuestionTypeRowMapper questiontypeMapper,
        QuestionRowMapper questionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Question.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.subjectMapper = subjectMapper;
        this.questiontypeMapper = questiontypeMapper;
        this.questionMapper = questionMapper;
    }

    @Override
    public Flux<Question> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Question> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = QuestionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SubjectSqlHelper.getColumns(subjectTable, "subject"));
        columns.addAll(QuestionTypeSqlHelper.getColumns(typeTable, "type"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(subjectTable)
            .on(Column.create("subject_id", entityTable))
            .equals(Column.create("id", subjectTable))
            .leftOuterJoin(typeTable)
            .on(Column.create("type_id", entityTable))
            .equals(Column.create("id", typeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Question.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Question> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Question> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Question> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Question> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Question> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Question process(Row row, RowMetadata metadata) {
        Question entity = questionMapper.apply(row, "e");
        entity.setSubject(subjectMapper.apply(row, "subject"));
        entity.setType(questiontypeMapper.apply(row, "type"));
        return entity;
    }

    @Override
    public <S extends Question> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
