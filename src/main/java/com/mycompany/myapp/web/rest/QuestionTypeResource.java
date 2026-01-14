package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.QuestionTypeRepository;
import com.mycompany.myapp.service.QuestionTypeService;
import com.mycompany.myapp.service.dto.QuestionTypeDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.QuestionType}.
 */
@RestController
@RequestMapping("/api/question-types")
public class QuestionTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionTypeResource.class);

    private static final String ENTITY_NAME = "questionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionTypeService questionTypeService;

    private final QuestionTypeRepository questionTypeRepository;

    public QuestionTypeResource(QuestionTypeService questionTypeService, QuestionTypeRepository questionTypeRepository) {
        this.questionTypeService = questionTypeService;
        this.questionTypeRepository = questionTypeRepository;
    }

    /**
     * {@code POST  /question-types} : Create a new questionType.
     *
     * @param questionTypeDTO the questionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionTypeDTO, or with status {@code 400 (Bad Request)} if the questionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<QuestionTypeDTO>> createQuestionType(@Valid @RequestBody QuestionTypeDTO questionTypeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save QuestionType : {}", questionTypeDTO);
        if (questionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return questionTypeService
            .save(questionTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/question-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /question-types/:id} : Updates an existing questionType.
     *
     * @param id the id of the questionTypeDTO to save.
     * @param questionTypeDTO the questionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the questionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<QuestionTypeDTO>> updateQuestionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuestionTypeDTO questionTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuestionType : {}, {}", id, questionTypeDTO);
        if (questionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return questionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return questionTypeService
                    .update(questionTypeDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /question-types/:id} : Partial updates given fields of an existing questionType, field will ignore if it is null
     *
     * @param id the id of the questionTypeDTO to save.
     * @param questionTypeDTO the questionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the questionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<QuestionTypeDTO>> partialUpdateQuestionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuestionTypeDTO questionTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuestionType partially : {}, {}", id, questionTypeDTO);
        if (questionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return questionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<QuestionTypeDTO> result = questionTypeService.partialUpdate(questionTypeDTO);

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
     * {@code GET  /question-types} : get all the questionTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<QuestionTypeDTO>> getAllQuestionTypes() {
        LOG.debug("REST request to get all QuestionTypes");
        return questionTypeService.findAll().collectList();
    }

    /**
     * {@code GET  /question-types} : get all the questionTypes as a stream.
     * @return the {@link Flux} of questionTypes.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<QuestionTypeDTO> getAllQuestionTypesAsStream() {
        LOG.debug("REST request to get all QuestionTypes as a stream");
        return questionTypeService.findAll();
    }

    /**
     * {@code GET  /question-types/:id} : get the "id" questionType.
     *
     * @param id the id of the questionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<QuestionTypeDTO>> getQuestionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuestionType : {}", id);
        Mono<QuestionTypeDTO> questionTypeDTO = questionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionTypeDTO);
    }

    /**
     * {@code DELETE  /question-types/:id} : delete the "id" questionType.
     *
     * @param id the id of the questionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteQuestionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuestionType : {}", id);
        return questionTypeService
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
