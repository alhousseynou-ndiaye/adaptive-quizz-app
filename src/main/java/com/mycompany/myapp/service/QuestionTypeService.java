package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.QuestionTypeRepository;
import com.mycompany.myapp.service.dto.QuestionTypeDTO;
import com.mycompany.myapp.service.mapper.QuestionTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.QuestionType}.
 */
@Service
@Transactional
public class QuestionTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionTypeService.class);

    private final QuestionTypeRepository questionTypeRepository;

    private final QuestionTypeMapper questionTypeMapper;

    public QuestionTypeService(QuestionTypeRepository questionTypeRepository, QuestionTypeMapper questionTypeMapper) {
        this.questionTypeRepository = questionTypeRepository;
        this.questionTypeMapper = questionTypeMapper;
    }

    /**
     * Save a questionType.
     *
     * @param questionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuestionTypeDTO> save(QuestionTypeDTO questionTypeDTO) {
        LOG.debug("Request to save QuestionType : {}", questionTypeDTO);
        return questionTypeRepository.save(questionTypeMapper.toEntity(questionTypeDTO)).map(questionTypeMapper::toDto);
    }

    /**
     * Update a questionType.
     *
     * @param questionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuestionTypeDTO> update(QuestionTypeDTO questionTypeDTO) {
        LOG.debug("Request to update QuestionType : {}", questionTypeDTO);
        return questionTypeRepository.save(questionTypeMapper.toEntity(questionTypeDTO)).map(questionTypeMapper::toDto);
    }

    /**
     * Partially update a questionType.
     *
     * @param questionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<QuestionTypeDTO> partialUpdate(QuestionTypeDTO questionTypeDTO) {
        LOG.debug("Request to partially update QuestionType : {}", questionTypeDTO);

        return questionTypeRepository
            .findById(questionTypeDTO.getId())
            .map(existingQuestionType -> {
                questionTypeMapper.partialUpdate(existingQuestionType, questionTypeDTO);

                return existingQuestionType;
            })
            .flatMap(questionTypeRepository::save)
            .map(questionTypeMapper::toDto);
    }

    /**
     * Get all the questionTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<QuestionTypeDTO> findAll() {
        LOG.debug("Request to get all QuestionTypes");
        return questionTypeRepository.findAll().map(questionTypeMapper::toDto);
    }

    /**
     * Returns the number of questionTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return questionTypeRepository.count();
    }

    /**
     * Get one questionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<QuestionTypeDTO> findOne(Long id) {
        LOG.debug("Request to get QuestionType : {}", id);
        return questionTypeRepository.findById(id).map(questionTypeMapper::toDto);
    }

    /**
     * Delete the questionType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete QuestionType : {}", id);
        return questionTypeRepository.deleteById(id);
    }
}
