package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.mapper.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Question}.
 */
@Service
@Transactional
public class QuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuestionDTO> save(QuestionDTO questionDTO) {
        LOG.debug("Request to save Question : {}", questionDTO);
        return questionRepository.save(questionMapper.toEntity(questionDTO)).map(questionMapper::toDto);
    }

    /**
     * Update a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuestionDTO> update(QuestionDTO questionDTO) {
        LOG.debug("Request to update Question : {}", questionDTO);
        return questionRepository.save(questionMapper.toEntity(questionDTO)).map(questionMapper::toDto);
    }

    /**
     * Partially update a question.
     *
     * @param questionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<QuestionDTO> partialUpdate(QuestionDTO questionDTO) {
        LOG.debug("Request to partially update Question : {}", questionDTO);

        return questionRepository
            .findById(questionDTO.getId())
            .map(existingQuestion -> {
                questionMapper.partialUpdate(existingQuestion, questionDTO);

                return existingQuestion;
            })
            .flatMap(questionRepository::save)
            .map(questionMapper::toDto);
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<QuestionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Questions");
        return questionRepository.findAllBy(pageable).map(questionMapper::toDto);
    }

    /**
     * Get all the questions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<QuestionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return questionRepository.findAllWithEagerRelationships(pageable).map(questionMapper::toDto);
    }

    /**
     * Returns the number of questions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return questionRepository.count();
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<QuestionDTO> findOne(Long id) {
        LOG.debug("Request to get Question : {}", id);
        return questionRepository.findOneWithEagerRelationships(id).map(questionMapper::toDto);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Question : {}", id);
        return questionRepository.deleteById(id);
    }
}
