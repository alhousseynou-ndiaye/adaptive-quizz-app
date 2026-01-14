package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.ReviewReminderRepository;
import com.mycompany.myapp.service.dto.ReviewReminderDTO;
import com.mycompany.myapp.service.mapper.ReviewReminderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ReviewReminder}.
 */
@Service
@Transactional
public class ReviewReminderService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewReminderService.class);

    private final ReviewReminderRepository reviewReminderRepository;

    private final ReviewReminderMapper reviewReminderMapper;

    public ReviewReminderService(ReviewReminderRepository reviewReminderRepository, ReviewReminderMapper reviewReminderMapper) {
        this.reviewReminderRepository = reviewReminderRepository;
        this.reviewReminderMapper = reviewReminderMapper;
    }

    /**
     * Save a reviewReminder.
     *
     * @param reviewReminderDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReviewReminderDTO> save(ReviewReminderDTO reviewReminderDTO) {
        LOG.debug("Request to save ReviewReminder : {}", reviewReminderDTO);
        return reviewReminderRepository.save(reviewReminderMapper.toEntity(reviewReminderDTO)).map(reviewReminderMapper::toDto);
    }

    /**
     * Update a reviewReminder.
     *
     * @param reviewReminderDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReviewReminderDTO> update(ReviewReminderDTO reviewReminderDTO) {
        LOG.debug("Request to update ReviewReminder : {}", reviewReminderDTO);
        return reviewReminderRepository.save(reviewReminderMapper.toEntity(reviewReminderDTO)).map(reviewReminderMapper::toDto);
    }

    /**
     * Partially update a reviewReminder.
     *
     * @param reviewReminderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReviewReminderDTO> partialUpdate(ReviewReminderDTO reviewReminderDTO) {
        LOG.debug("Request to partially update ReviewReminder : {}", reviewReminderDTO);

        return reviewReminderRepository
            .findById(reviewReminderDTO.getId())
            .map(existingReviewReminder -> {
                reviewReminderMapper.partialUpdate(existingReviewReminder, reviewReminderDTO);

                return existingReviewReminder;
            })
            .flatMap(reviewReminderRepository::save)
            .map(reviewReminderMapper::toDto);
    }

    /**
     * Get all the reviewReminders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReviewReminderDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ReviewReminders");
        return reviewReminderRepository.findAllBy(pageable).map(reviewReminderMapper::toDto);
    }

    /**
     * Get all the reviewReminders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ReviewReminderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewReminderRepository.findAllWithEagerRelationships(pageable).map(reviewReminderMapper::toDto);
    }

    /**
     * Returns the number of reviewReminders available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reviewReminderRepository.count();
    }

    /**
     * Get one reviewReminder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReviewReminderDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewReminder : {}", id);
        return reviewReminderRepository.findOneWithEagerRelationships(id).map(reviewReminderMapper::toDto);
    }

    /**
     * Delete the reviewReminder by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete ReviewReminder : {}", id);
        return reviewReminderRepository.deleteById(id);
    }
}
