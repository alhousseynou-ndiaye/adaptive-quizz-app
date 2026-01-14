package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AnswerAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Answer;
import com.mycompany.myapp.repository.AnswerRepository;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.AnswerService;
import com.mycompany.myapp.service.dto.AnswerDTO;
import com.mycompany.myapp.service.mapper.AnswerMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link AnswerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AnswerResourceIT {

    private static final Instant DEFAULT_ANSWERED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ANSWERED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_CORRECT = false;
    private static final Boolean UPDATED_IS_CORRECT = true;

    private static final Long DEFAULT_TIME_SPENT_MS = 0L;
    private static final Long UPDATED_TIME_SPENT_MS = 1L;

    private static final Integer DEFAULT_SELF_REPORTED_RECALL = 0;
    private static final Integer UPDATED_SELF_REPORTED_RECALL = 1;

    private static final Integer DEFAULT_DIFFICULTY_AT_ANSWER = 1;
    private static final Integer UPDATED_DIFFICULTY_AT_ANSWER = 2;

    private static final String DEFAULT_SELECTED_CHOICE = "A";
    private static final String UPDATED_SELECTED_CHOICE = "C";

    private static final String DEFAULT_FREE_TEXT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_FREE_TEXT_ANSWER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private AnswerRepository answerRepositoryMock;

    @Autowired
    private AnswerMapper answerMapper;

    @Mock
    private AnswerService answerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Answer answer;

    private Answer insertedAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Answer createEntity() {
        return new Answer()
            .answeredAt(DEFAULT_ANSWERED_AT)
            .isCorrect(DEFAULT_IS_CORRECT)
            .timeSpentMs(DEFAULT_TIME_SPENT_MS)
            .selfReportedRecall(DEFAULT_SELF_REPORTED_RECALL)
            .difficultyAtAnswer(DEFAULT_DIFFICULTY_AT_ANSWER)
            .selectedChoice(DEFAULT_SELECTED_CHOICE)
            .freeTextAnswer(DEFAULT_FREE_TEXT_ANSWER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Answer createUpdatedEntity() {
        return new Answer()
            .answeredAt(UPDATED_ANSWERED_AT)
            .isCorrect(UPDATED_IS_CORRECT)
            .timeSpentMs(UPDATED_TIME_SPENT_MS)
            .selfReportedRecall(UPDATED_SELF_REPORTED_RECALL)
            .difficultyAtAnswer(UPDATED_DIFFICULTY_AT_ANSWER)
            .selectedChoice(UPDATED_SELECTED_CHOICE)
            .freeTextAnswer(UPDATED_FREE_TEXT_ANSWER);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Answer.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        answer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAnswer != null) {
            answerRepository.delete(insertedAnswer).block();
            insertedAnswer = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createAnswer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);
        var returnedAnswerDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AnswerDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Answer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAnswer = answerMapper.toEntity(returnedAnswerDTO);
        assertAnswerUpdatableFieldsEquals(returnedAnswer, getPersistedAnswer(returnedAnswer));

        insertedAnswer = returnedAnswer;
    }

    @Test
    void createAnswerWithExistingId() throws Exception {
        // Create the Answer with an existing ID
        answer.setId(1L);
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Answer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkAnsweredAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        answer.setAnsweredAt(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIsCorrectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        answer.setIsCorrect(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSelfReportedRecallIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        answer.setSelfReportedRecall(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDifficultyAtAnswerIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        answer.setDifficultyAtAnswer(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAnswers() {
        // Initialize the database
        insertedAnswer = answerRepository.save(answer).block();

        // Get all the answerList
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
            .value(hasItem(answer.getId().intValue()))
            .jsonPath("$.[*].answeredAt")
            .value(hasItem(DEFAULT_ANSWERED_AT.toString()))
            .jsonPath("$.[*].isCorrect")
            .value(hasItem(DEFAULT_IS_CORRECT))
            .jsonPath("$.[*].timeSpentMs")
            .value(hasItem(DEFAULT_TIME_SPENT_MS.intValue()))
            .jsonPath("$.[*].selfReportedRecall")
            .value(hasItem(DEFAULT_SELF_REPORTED_RECALL))
            .jsonPath("$.[*].difficultyAtAnswer")
            .value(hasItem(DEFAULT_DIFFICULTY_AT_ANSWER))
            .jsonPath("$.[*].selectedChoice")
            .value(hasItem(DEFAULT_SELECTED_CHOICE))
            .jsonPath("$.[*].freeTextAnswer")
            .value(hasItem(DEFAULT_FREE_TEXT_ANSWER));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAnswersWithEagerRelationshipsIsEnabled() {
        when(answerServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(answerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAnswersWithEagerRelationshipsIsNotEnabled() {
        when(answerServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(answerRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getAnswer() {
        // Initialize the database
        insertedAnswer = answerRepository.save(answer).block();

        // Get the answer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, answer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(answer.getId().intValue()))
            .jsonPath("$.answeredAt")
            .value(is(DEFAULT_ANSWERED_AT.toString()))
            .jsonPath("$.isCorrect")
            .value(is(DEFAULT_IS_CORRECT))
            .jsonPath("$.timeSpentMs")
            .value(is(DEFAULT_TIME_SPENT_MS.intValue()))
            .jsonPath("$.selfReportedRecall")
            .value(is(DEFAULT_SELF_REPORTED_RECALL))
            .jsonPath("$.difficultyAtAnswer")
            .value(is(DEFAULT_DIFFICULTY_AT_ANSWER))
            .jsonPath("$.selectedChoice")
            .value(is(DEFAULT_SELECTED_CHOICE))
            .jsonPath("$.freeTextAnswer")
            .value(is(DEFAULT_FREE_TEXT_ANSWER));
    }

    @Test
    void getNonExistingAnswer() {
        // Get the answer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAnswer() throws Exception {
        // Initialize the database
        insertedAnswer = answerRepository.save(answer).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the answer
        Answer updatedAnswer = answerRepository.findById(answer.getId()).block();
        updatedAnswer
            .answeredAt(UPDATED_ANSWERED_AT)
            .isCorrect(UPDATED_IS_CORRECT)
            .timeSpentMs(UPDATED_TIME_SPENT_MS)
            .selfReportedRecall(UPDATED_SELF_REPORTED_RECALL)
            .difficultyAtAnswer(UPDATED_DIFFICULTY_AT_ANSWER)
            .selectedChoice(UPDATED_SELECTED_CHOICE)
            .freeTextAnswer(UPDATED_FREE_TEXT_ANSWER);
        AnswerDTO answerDTO = answerMapper.toDto(updatedAnswer);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, answerDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Answer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnswerToMatchAllProperties(updatedAnswer);
    }

    @Test
    void putNonExistingAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        answer.setId(longCount.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, answerDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Answer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        answer.setId(longCount.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Answer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        answer.setId(longCount.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Answer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAnswerWithPatch() throws Exception {
        // Initialize the database
        insertedAnswer = answerRepository.save(answer).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the answer using partial update
        Answer partialUpdatedAnswer = new Answer();
        partialUpdatedAnswer.setId(answer.getId());

        partialUpdatedAnswer
            .isCorrect(UPDATED_IS_CORRECT)
            .difficultyAtAnswer(UPDATED_DIFFICULTY_AT_ANSWER)
            .freeTextAnswer(UPDATED_FREE_TEXT_ANSWER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAnswer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAnswer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Answer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnswerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAnswer, answer), getPersistedAnswer(answer));
    }

    @Test
    void fullUpdateAnswerWithPatch() throws Exception {
        // Initialize the database
        insertedAnswer = answerRepository.save(answer).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the answer using partial update
        Answer partialUpdatedAnswer = new Answer();
        partialUpdatedAnswer.setId(answer.getId());

        partialUpdatedAnswer
            .answeredAt(UPDATED_ANSWERED_AT)
            .isCorrect(UPDATED_IS_CORRECT)
            .timeSpentMs(UPDATED_TIME_SPENT_MS)
            .selfReportedRecall(UPDATED_SELF_REPORTED_RECALL)
            .difficultyAtAnswer(UPDATED_DIFFICULTY_AT_ANSWER)
            .selectedChoice(UPDATED_SELECTED_CHOICE)
            .freeTextAnswer(UPDATED_FREE_TEXT_ANSWER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAnswer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAnswer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Answer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnswerUpdatableFieldsEquals(partialUpdatedAnswer, getPersistedAnswer(partialUpdatedAnswer));
    }

    @Test
    void patchNonExistingAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        answer.setId(longCount.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, answerDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Answer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        answer.setId(longCount.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Answer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        answer.setId(longCount.incrementAndGet());

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(answerDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Answer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAnswer() {
        // Initialize the database
        insertedAnswer = answerRepository.save(answer).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the answer
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, answer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return answerRepository.count().block();
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

    protected Answer getPersistedAnswer(Answer answer) {
        return answerRepository.findById(answer.getId()).block();
    }

    protected void assertPersistedAnswerToMatchAllProperties(Answer expectedAnswer) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAnswerAllPropertiesEquals(expectedAnswer, getPersistedAnswer(expectedAnswer));
        assertAnswerUpdatableFieldsEquals(expectedAnswer, getPersistedAnswer(expectedAnswer));
    }

    protected void assertPersistedAnswerToMatchUpdatableProperties(Answer expectedAnswer) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAnswerAllUpdatablePropertiesEquals(expectedAnswer, getPersistedAnswer(expectedAnswer));
        assertAnswerUpdatableFieldsEquals(expectedAnswer, getPersistedAnswer(expectedAnswer));
    }
}
