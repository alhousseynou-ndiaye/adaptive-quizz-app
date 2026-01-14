package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.UserStatsRepository;
import com.mycompany.myapp.service.dto.UserStatsDTO;
import com.mycompany.myapp.service.mapper.UserStatsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.UserStats}.
 */
@Service
@Transactional
public class UserStatsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserStatsService.class);

    private final UserStatsRepository userStatsRepository;

    private final UserStatsMapper userStatsMapper;

    public UserStatsService(UserStatsRepository userStatsRepository, UserStatsMapper userStatsMapper) {
        this.userStatsRepository = userStatsRepository;
        this.userStatsMapper = userStatsMapper;
    }

    /**
     * Save a userStats.
     *
     * @param userStatsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserStatsDTO> save(UserStatsDTO userStatsDTO) {
        LOG.debug("Request to save UserStats : {}", userStatsDTO);
        return userStatsRepository.save(userStatsMapper.toEntity(userStatsDTO)).map(userStatsMapper::toDto);
    }

    /**
     * Update a userStats.
     *
     * @param userStatsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserStatsDTO> update(UserStatsDTO userStatsDTO) {
        LOG.debug("Request to update UserStats : {}", userStatsDTO);
        return userStatsRepository.save(userStatsMapper.toEntity(userStatsDTO)).map(userStatsMapper::toDto);
    }

    /**
     * Partially update a userStats.
     *
     * @param userStatsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UserStatsDTO> partialUpdate(UserStatsDTO userStatsDTO) {
        LOG.debug("Request to partially update UserStats : {}", userStatsDTO);

        return userStatsRepository
            .findById(userStatsDTO.getId())
            .map(existingUserStats -> {
                userStatsMapper.partialUpdate(existingUserStats, userStatsDTO);

                return existingUserStats;
            })
            .flatMap(userStatsRepository::save)
            .map(userStatsMapper::toDto);
    }

    /**
     * Get all the userStats.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserStatsDTO> findAll() {
        LOG.debug("Request to get all UserStats");
        return userStatsRepository.findAll().map(userStatsMapper::toDto);
    }

    /**
     * Get all the userStats with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<UserStatsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userStatsRepository.findAllWithEagerRelationships(pageable).map(userStatsMapper::toDto);
    }

    /**
     * Returns the number of userStats available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return userStatsRepository.count();
    }

    /**
     * Get one userStats by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UserStatsDTO> findOne(Long id) {
        LOG.debug("Request to get UserStats : {}", id);
        return userStatsRepository.findOneWithEagerRelationships(id).map(userStatsMapper::toDto);
    }

    /**
     * Delete the userStats by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete UserStats : {}", id);
        return userStatsRepository.deleteById(id);
    }
}
