package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserStats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserStats entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserStatsRepository extends ReactiveCrudRepository<UserStats, Long>, UserStatsRepositoryInternal {
    @Override
    Mono<UserStats> findOneWithEagerRelationships(Long id);

    @Override
    Flux<UserStats> findAllWithEagerRelationships();

    @Override
    Flux<UserStats> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM user_stats entity WHERE entity.user_id = :id")
    Flux<UserStats> findByUser(Long id);

    @Query("SELECT * FROM user_stats entity WHERE entity.user_id IS NULL")
    Flux<UserStats> findAllWhereUserIsNull();

    @Query("SELECT * FROM user_stats entity WHERE entity.subject_id = :id")
    Flux<UserStats> findBySubject(Long id);

    @Query("SELECT * FROM user_stats entity WHERE entity.subject_id IS NULL")
    Flux<UserStats> findAllWhereSubjectIsNull();

    @Override
    <S extends UserStats> Mono<S> save(S entity);

    @Override
    Flux<UserStats> findAll();

    @Override
    Mono<UserStats> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserStatsRepositoryInternal {
    <S extends UserStats> Mono<S> save(S entity);

    Flux<UserStats> findAllBy(Pageable pageable);

    Flux<UserStats> findAll();

    Mono<UserStats> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserStats> findAllBy(Pageable pageable, Criteria criteria);

    Mono<UserStats> findOneWithEagerRelationships(Long id);

    Flux<UserStats> findAllWithEagerRelationships();

    Flux<UserStats> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
