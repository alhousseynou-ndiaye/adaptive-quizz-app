package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends ReactiveCrudRepository<Question, Long>, QuestionRepositoryInternal {
    Flux<Question> findAllBy(Pageable pageable);

    @Override
    Mono<Question> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Question> findAllWithEagerRelationships();

    @Override
    Flux<Question> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM question entity WHERE entity.subject_id = :id")
    Flux<Question> findBySubject(Long id);

    @Query("SELECT * FROM question entity WHERE entity.subject_id IS NULL")
    Flux<Question> findAllWhereSubjectIsNull();

    @Query("SELECT * FROM question entity WHERE entity.type_id = :id")
    Flux<Question> findByType(Long id);

    @Query("SELECT * FROM question entity WHERE entity.type_id IS NULL")
    Flux<Question> findAllWhereTypeIsNull();

    @Override
    <S extends Question> Mono<S> save(S entity);

    @Override
    Flux<Question> findAll();

    @Override
    Mono<Question> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface QuestionRepositoryInternal {
    <S extends Question> Mono<S> save(S entity);

    Flux<Question> findAllBy(Pageable pageable);

    Flux<Question> findAll();

    Mono<Question> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Question> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Question> findOneWithEagerRelationships(Long id);

    Flux<Question> findAllWithEagerRelationships();

    Flux<Question> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
