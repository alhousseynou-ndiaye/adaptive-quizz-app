package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.QuestionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the QuestionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionTypeRepository extends ReactiveCrudRepository<QuestionType, Long>, QuestionTypeRepositoryInternal {
    @Override
    <S extends QuestionType> Mono<S> save(S entity);

    @Override
    Flux<QuestionType> findAll();

    @Override
    Mono<QuestionType> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface QuestionTypeRepositoryInternal {
    <S extends QuestionType> Mono<S> save(S entity);

    Flux<QuestionType> findAllBy(Pageable pageable);

    Flux<QuestionType> findAll();

    Mono<QuestionType> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<QuestionType> findAllBy(Pageable pageable, Criteria criteria);
}
