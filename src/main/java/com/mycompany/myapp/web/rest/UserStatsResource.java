package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.UserStatsRepository;
import com.mycompany.myapp.service.UserStatsService;
import com.mycompany.myapp.service.dto.UserStatsDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.UserStats}.
 */
@RestController
@RequestMapping("/api/user-stats")
public class UserStatsResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserStatsResource.class);

    private static final String ENTITY_NAME = "userStats";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserStatsService userStatsService;

    private final UserStatsRepository userStatsRepository;

    public UserStatsResource(UserStatsService userStatsService, UserStatsRepository userStatsRepository) {
        this.userStatsService = userStatsService;
        this.userStatsRepository = userStatsRepository;
    }

    /**
     * {@code POST  /user-stats} : Create a new userStats.
     *
     * @param userStatsDTO the userStatsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userStatsDTO, or with status {@code 400 (Bad Request)} if the userStats has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserStatsDTO>> createUserStats(@Valid @RequestBody UserStatsDTO userStatsDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserStats : {}", userStatsDTO);
        if (userStatsDTO.getId() != null) {
            throw new BadRequestAlertException("A new userStats cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userStatsService
            .save(userStatsDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/user-stats/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-stats/:id} : Updates an existing userStats.
     *
     * @param id the id of the userStatsDTO to save.
     * @param userStatsDTO the userStatsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userStatsDTO,
     * or with status {@code 400 (Bad Request)} if the userStatsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userStatsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserStatsDTO>> updateUserStats(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserStatsDTO userStatsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserStats : {}, {}", id, userStatsDTO);
        if (userStatsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userStatsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userStatsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userStatsService
                    .update(userStatsDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /user-stats/:id} : Partial updates given fields of an existing userStats, field will ignore if it is null
     *
     * @param id the id of the userStatsDTO to save.
     * @param userStatsDTO the userStatsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userStatsDTO,
     * or with status {@code 400 (Bad Request)} if the userStatsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userStatsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userStatsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserStatsDTO>> partialUpdateUserStats(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserStatsDTO userStatsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserStats partially : {}, {}", id, userStatsDTO);
        if (userStatsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userStatsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userStatsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserStatsDTO> result = userStatsService.partialUpdate(userStatsDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /user-stats} : get all the userStats.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userStats in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<UserStatsDTO>> getAllUserStats(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all UserStats");
        return userStatsService.findAll().collectList();
    }

    /**
     * {@code GET  /user-stats} : get all the userStats as a stream.
     * @return the {@link Flux} of userStats.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UserStatsDTO> getAllUserStatsAsStream() {
        LOG.debug("REST request to get all UserStats as a stream");
        return userStatsService.findAll();
    }

    /**
     * {@code GET  /user-stats/:id} : get the "id" userStats.
     *
     * @param id the id of the userStatsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userStatsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserStatsDTO>> getUserStats(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserStats : {}", id);
        Mono<UserStatsDTO> userStatsDTO = userStatsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userStatsDTO);
    }

    /**
     * {@code DELETE  /user-stats/:id} : delete the "id" userStats.
     *
     * @param id the id of the userStatsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserStats(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserStats : {}", id);
        return userStatsService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
