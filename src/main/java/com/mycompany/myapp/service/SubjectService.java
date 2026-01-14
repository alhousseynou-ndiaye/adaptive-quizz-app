package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.SubjectRepository;
import com.mycompany.myapp.service.dto.SubjectDTO;
import com.mycompany.myapp.service.mapper.SubjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Subject}.
 */
@Service
@Transactional
public class SubjectService {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepository subjectRepository;

    private final SubjectMapper subjectMapper;

    public SubjectService(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    /**
     * Save a subject.
     *
     * @param subjectDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SubjectDTO> save(SubjectDTO subjectDTO) {
        LOG.debug("Request to save Subject : {}", subjectDTO);
        return subjectRepository.save(subjectMapper.toEntity(subjectDTO)).map(subjectMapper::toDto);
    }

    /**
     * Update a subject.
     *
     * @param subjectDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SubjectDTO> update(SubjectDTO subjectDTO) {
        LOG.debug("Request to update Subject : {}", subjectDTO);
        return subjectRepository.save(subjectMapper.toEntity(subjectDTO)).map(subjectMapper::toDto);
    }

    /**
     * Partially update a subject.
     *
     * @param subjectDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SubjectDTO> partialUpdate(SubjectDTO subjectDTO) {
        LOG.debug("Request to partially update Subject : {}", subjectDTO);

        return subjectRepository
            .findById(subjectDTO.getId())
            .map(existingSubject -> {
                subjectMapper.partialUpdate(existingSubject, subjectDTO);

                return existingSubject;
            })
            .flatMap(subjectRepository::save)
            .map(subjectMapper::toDto);
    }

    /**
     * Get all the subjects.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SubjectDTO> findAll() {
        LOG.debug("Request to get all Subjects");
        return subjectRepository.findAll().map(subjectMapper::toDto);
    }

    /**
     * Returns the number of subjects available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return subjectRepository.count();
    }

    /**
     * Get one subject by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SubjectDTO> findOne(Long id) {
        LOG.debug("Request to get Subject : {}", id);
        return subjectRepository.findById(id).map(subjectMapper::toDto);
    }

    /**
     * Delete the subject by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Subject : {}", id);
        return subjectRepository.deleteById(id);
    }
}
