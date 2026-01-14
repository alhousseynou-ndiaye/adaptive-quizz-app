package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ReviewReminderRepository;
import com.mycompany.myapp.service.ReviewReminderService;
import com.mycompany.myapp.service.dto.ReviewReminderDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ReviewReminder}.
 */
@RestController
@RequestMapping("/api/review-reminders")
public class ReviewReminderResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewReminderResource.class);

    private static final String ENTITY_NAME = "reviewReminder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewReminderService reviewReminderService;

    private final ReviewReminderRepository reviewReminderRepository;

    public ReviewReminderResource(ReviewReminderService reviewReminderService, ReviewReminderRepository reviewReminderRepository) {
        this.reviewReminderService = reviewReminderService;
        this.reviewReminderRepository = reviewReminderRepository;
    }

    /**
     * {@code POST  /review-reminders} : Create a new reviewReminder.
     *
     * @param reviewReminderDTO the reviewReminderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewReminderDTO, or with status {@code 400 (Bad Request)} if the reviewReminder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ReviewReminderDTO>> createReviewReminder(@Valid @RequestBody ReviewReminderDTO reviewReminderDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReviewReminder : {}", reviewReminderDTO);
        if (reviewReminderDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewReminder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return reviewReminderService
            .save(reviewReminderDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/review-reminders/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /review-reminders/:id} : Updates an existing reviewReminder.
     *
     * @param id the id of the reviewReminderDTO to save.
     * @param reviewReminderDTO the reviewReminderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewReminderDTO,
     * or with status {@code 400 (Bad Request)} if the reviewReminderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewReminderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ReviewReminderDTO>> updateReviewReminder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewReminderDTO reviewReminderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewReminder : {}, {}", id, reviewReminderDTO);
        if (reviewReminderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewReminderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reviewReminderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reviewReminderService
                    .update(reviewReminderDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /review-reminders/:id} : Partial updates given fields of an existing reviewReminder, field will ignore if it is null
     *
     * @param id the id of the reviewReminderDTO to save.
     * @param reviewReminderDTO the reviewReminderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewReminderDTO,
     * or with status {@code 400 (Bad Request)} if the reviewReminderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reviewReminderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewReminderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReviewReminderDTO>> partialUpdateReviewReminder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewReminderDTO reviewReminderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewReminder partially : {}, {}", id, reviewReminderDTO);
        if (reviewReminderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewReminderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reviewReminderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReviewReminderDTO> result = reviewReminderService.partialUpdate(reviewReminderDTO);

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
     * {@code GET  /review-reminders} : get all the reviewReminders.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewReminders in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ReviewReminderDTO>>> getAllReviewReminders(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ReviewReminders");
        return reviewReminderService
            .countAll()
            .zipWith(reviewReminderService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /review-reminders/:id} : get the "id" reviewReminder.
     *
     * @param id the id of the reviewReminderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewReminderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReviewReminderDTO>> getReviewReminder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewReminder : {}", id);
        Mono<ReviewReminderDTO> reviewReminderDTO = reviewReminderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewReminderDTO);
    }

    /**
     * {@code DELETE  /review-reminders/:id} : delete the "id" reviewReminder.
     *
     * @param id the id of the reviewReminderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReviewReminder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewReminder : {}", id);
        return reviewReminderService
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
