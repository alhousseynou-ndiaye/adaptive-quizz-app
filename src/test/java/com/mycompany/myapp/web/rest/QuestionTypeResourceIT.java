package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.QuestionTypeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.QuestionType;
import com.mycompany.myapp.domain.enumeration.QuestionTypeCode;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.QuestionTypeRepository;
import com.mycompany.myapp.service.dto.QuestionTypeDTO;
import com.mycompany.myapp.service.mapper.QuestionTypeMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link QuestionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class QuestionTypeResourceIT {

    private static final QuestionTypeCode DEFAULT_CODE = QuestionTypeCode.MCQ;
    private static final QuestionTypeCode UPDATED_CODE = QuestionTypeCode.TRUE_FALSE;

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionTypeRepository questionTypeRepository;

    @Autowired
    private QuestionTypeMapper questionTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private QuestionType questionType;

    private QuestionType insertedQuestionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionType createEntity() {
        return new QuestionType().code(DEFAULT_CODE).label(DEFAULT_LABEL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionType createUpdatedEntity() {
        return new QuestionType().code(UPDATED_CODE).label(UPDATED_LABEL);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(QuestionType.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        questionType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestionType != null) {
            questionTypeRepository.delete(insertedQuestionType).block();
            insertedQuestionType = null;
        }
        deleteEntities(em);
    }

    @Test
    void createQuestionType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);
        var returnedQuestionTypeDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(QuestionTypeDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the QuestionType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestionType = questionTypeMapper.toEntity(returnedQuestionTypeDTO);
        assertQuestionTypeUpdatableFieldsEquals(returnedQuestionType, getPersistedQuestionType(returnedQuestionType));

        insertedQuestionType = returnedQuestionType;
    }

    @Test
    void createQuestionTypeWithExistingId() throws Exception {
        // Create the QuestionType with an existing ID
        questionType.setId(1L);
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        questionType.setCode(null);

        // Create the QuestionType, which fails.
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        questionType.setLabel(null);

        // Create the QuestionType, which fails.
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllQuestionTypesAsStream() {
        // Initialize the database
        questionTypeRepository.save(questionType).block();

        List<QuestionType> questionTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(QuestionTypeDTO.class)
            .getResponseBody()
            .map(questionTypeMapper::toEntity)
            .filter(questionType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(questionTypeList).isNotNull();
        assertThat(questionTypeList).hasSize(1);
        QuestionType testQuestionType = questionTypeList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionTypeAllPropertiesEquals(questionType, testQuestionType);
        assertQuestionTypeUpdatableFieldsEquals(questionType, testQuestionType);
    }

    @Test
    void getAllQuestionTypes() {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.save(questionType).block();

        // Get all the questionTypeList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(questionType.getId().intValue()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE.toString()))
            .jsonPath("$.[*].label")
            .value(hasItem(DEFAULT_LABEL));
    }

    @Test
    void getQuestionType() {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.save(questionType).block();

        // Get the questionType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, questionType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(questionType.getId().intValue()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE.toString()))
            .jsonPath("$.label")
            .value(is(DEFAULT_LABEL));
    }

    @Test
    void getNonExistingQuestionType() {
        // Get the questionType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingQuestionType() throws Exception {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.save(questionType).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionType
        QuestionType updatedQuestionType = questionTypeRepository.findById(questionType.getId()).block();
        updatedQuestionType.code(UPDATED_CODE).label(UPDATED_LABEL);
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(updatedQuestionType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, questionTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionTypeToMatchAllProperties(updatedQuestionType);
    }

    @Test
    void putNonExistingQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, questionTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQuestionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.save(questionType).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionType using partial update
        QuestionType partialUpdatedQuestionType = new QuestionType();
        partialUpdatedQuestionType.setId(questionType.getId());

        partialUpdatedQuestionType.label(UPDATED_LABEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestionType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQuestionType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the QuestionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuestionType, questionType),
            getPersistedQuestionType(questionType)
        );
    }

    @Test
    void fullUpdateQuestionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.save(questionType).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionType using partial update
        QuestionType partialUpdatedQuestionType = new QuestionType();
        partialUpdatedQuestionType.setId(questionType.getId());

        partialUpdatedQuestionType.code(UPDATED_CODE).label(UPDATED_LABEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestionType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQuestionType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the QuestionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionTypeUpdatableFieldsEquals(partialUpdatedQuestionType, getPersistedQuestionType(partialUpdatedQuestionType));
    }

    @Test
    void patchNonExistingQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, questionTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQuestionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionType.setId(longCount.incrementAndGet());

        // Create the QuestionType
        QuestionTypeDTO questionTypeDTO = questionTypeMapper.toDto(questionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the QuestionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQuestionType() {
        // Initialize the database
        insertedQuestionType = questionTypeRepository.save(questionType).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the questionType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, questionType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionTypeRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected QuestionType getPersistedQuestionType(QuestionType questionType) {
        return questionTypeRepository.findById(questionType.getId()).block();
    }

    protected void assertPersistedQuestionTypeToMatchAllProperties(QuestionType expectedQuestionType) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionTypeAllPropertiesEquals(expectedQuestionType, getPersistedQuestionType(expectedQuestionType));
        assertQuestionTypeUpdatableFieldsEquals(expectedQuestionType, getPersistedQuestionType(expectedQuestionType));
    }

    protected void assertPersistedQuestionTypeToMatchUpdatableProperties(QuestionType expectedQuestionType) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionTypeAllUpdatablePropertiesEquals(expectedQuestionType, getPersistedQuestionType(expectedQuestionType));
        assertQuestionTypeUpdatableFieldsEquals(expectedQuestionType, getPersistedQuestionType(expectedQuestionType));
    }
}
