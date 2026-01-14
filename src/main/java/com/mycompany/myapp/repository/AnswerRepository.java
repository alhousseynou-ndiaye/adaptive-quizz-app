package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Answer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends ReactiveCrudRepository<Answer, Long>, AnswerRepositoryInternal {
    Flux<Answer> findAllBy(Pageable pageable);

    @Override
    Mono<Answer> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Answer> findAllWithEagerRelationships();

    @Override
    Flux<Answer> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM answer entity WHERE entity.user_id = :id")
    Flux<Answer> findByUser(Long id);

    @Query("SELECT * FROM answer entity WHERE entity.user_id IS NULL")
    Flux<Answer> findAllWhereUserIsNull();

    @Query("SELECT * FROM answer entity WHERE entity.question_id = :id")
    Flux<Answer> findByQuestion(Long id);

    @Query("SELECT * FROM answer entity WHERE entity.question_id IS NULL")
    Flux<Answer> findAllWhereQuestionIsNull();

    @Override
    <S extends Answer> Mono<S> save(S entity);

    @Override
    Flux<Answer> findAll();

    @Override
    Mono<Answer> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AnswerRepositoryInternal {
    <S extends Answer> Mono<S> save(S entity);

    Flux<Answer> findAllBy(Pageable pageable);

    Flux<Answer> findAll();

    Mono<Answer> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Answer> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Answer> findOneWithEagerRelationships(Long id);

    Flux<Answer> findAllWithEagerRelationships();

    Flux<Answer> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
