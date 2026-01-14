package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReviewReminder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ReviewReminder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewReminderRepository extends ReactiveCrudRepository<ReviewReminder, Long>, ReviewReminderRepositoryInternal {
    Flux<ReviewReminder> findAllBy(Pageable pageable);

    @Override
    Mono<ReviewReminder> findOneWithEagerRelationships(Long id);

    @Override
    Flux<ReviewReminder> findAllWithEagerRelationships();

    @Override
    Flux<ReviewReminder> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM review_reminder entity WHERE entity.user_id = :id")
    Flux<ReviewReminder> findByUser(Long id);

    @Query("SELECT * FROM review_reminder entity WHERE entity.user_id IS NULL")
    Flux<ReviewReminder> findAllWhereUserIsNull();

    @Query("SELECT * FROM review_reminder entity WHERE entity.question_id = :id")
    Flux<ReviewReminder> findByQuestion(Long id);

    @Query("SELECT * FROM review_reminder entity WHERE entity.question_id IS NULL")
    Flux<ReviewReminder> findAllWhereQuestionIsNull();

    @Override
    <S extends ReviewReminder> Mono<S> save(S entity);

    @Override
    Flux<ReviewReminder> findAll();

    @Override
    Mono<ReviewReminder> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ReviewReminderRepositoryInternal {
    <S extends ReviewReminder> Mono<S> save(S entity);

    Flux<ReviewReminder> findAllBy(Pageable pageable);

    Flux<ReviewReminder> findAll();

    Mono<ReviewReminder> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReviewReminder> findAllBy(Pageable pageable, Criteria criteria);

    Mono<ReviewReminder> findOneWithEagerRelationships(Long id);

    Flux<ReviewReminder> findAllWithEagerRelationships();

    Flux<ReviewReminder> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
