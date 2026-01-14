package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.QuestionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.service.QuestionService;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.mapper.QuestionMapper;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class QuestionResourceIT {

    private static final String DEFAULT_PROMPT = "AAAAAAAAAA";
    private static final String UPDATED_PROMPT = "BBBBBBBBBB";

    private static final Integer DEFAULT_DIFFICULTY = 1;
    private static final Integer UPDATED_DIFFICULTY = 2;

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_CHOICE_A = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE_A = "BBBBBBBBBB";

    private static final String DEFAULT_CHOICE_B = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE_B = "BBBBBBBBBB";

    private static final String DEFAULT_CHOICE_C = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE_C = "BBBBBBBBBB";

    private static final String DEFAULT_CHOICE_D = "AAAAAAAAAA";
    private static final String UPDATED_CHOICE_D = "BBBBBBBBBB";

    private static final String DEFAULT_CORRECT_CHOICE = "A";
    private static final String UPDATED_CORRECT_CHOICE = "D";

    private static final String DEFAULT_CORRECT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CORRECT_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionRepository questionRepository;

    @Mock
    private QuestionRepository questionRepositoryMock;

    @Autowired
    private QuestionMapper questionMapper;

    @Mock
    private QuestionService questionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Question question;

    private Question insertedQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity() {
        return new Question()
            .prompt(DEFAULT_PROMPT)
            .difficulty(DEFAULT_DIFFICULTY)
            .explanation(DEFAULT_EXPLANATION)
            .active(DEFAULT_ACTIVE)
            .choiceA(DEFAULT_CHOICE_A)
            .choiceB(DEFAULT_CHOICE_B)
            .choiceC(DEFAULT_CHOICE_C)
            .choiceD(DEFAULT_CHOICE_D)
            .correctChoice(DEFAULT_CORRECT_CHOICE)
            .correctText(DEFAULT_CORRECT_TEXT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity() {
        return new Question()
            .prompt(UPDATED_PROMPT)
            .difficulty(UPDATED_DIFFICULTY)
            .explanation(UPDATED_EXPLANATION)
            .active(UPDATED_ACTIVE)
            .choiceA(UPDATED_CHOICE_A)
            .choiceB(UPDATED_CHOICE_B)
            .choiceC(UPDATED_CHOICE_C)
            .choiceD(UPDATED_CHOICE_D)
            .correctChoice(UPDATED_CORRECT_CHOICE)
            .correctText(UPDATED_CORRECT_TEXT);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Question.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        question = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestion != null) {
            questionRepository.delete(insertedQuestion).block();
            insertedQuestion = null;
        }
        deleteEntities(em);
    }

    @Test
    void createQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        var returnedQuestionDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(QuestionDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Question in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestion = questionMapper.toEntity(returnedQuestionDTO);
        assertQuestionUpdatableFieldsEquals(returnedQuestion, getPersistedQuestion(returnedQuestion));

        insertedQuestion = returnedQuestion;
    }

    @Test
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkDifficultyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setDifficulty(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setActive(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllQuestions() {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        // Get all the questionList
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
            .value(hasItem(question.getId().intValue()))
            .jsonPath("$.[*].prompt")
            .value(hasItem(DEFAULT_PROMPT))
            .jsonPath("$.[*].difficulty")
            .value(hasItem(DEFAULT_DIFFICULTY))
            .jsonPath("$.[*].explanation")
            .value(hasItem(DEFAULT_EXPLANATION))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE))
            .jsonPath("$.[*].choiceA")
            .value(hasItem(DEFAULT_CHOICE_A))
            .jsonPath("$.[*].choiceB")
            .value(hasItem(DEFAULT_CHOICE_B))
            .jsonPath("$.[*].choiceC")
            .value(hasItem(DEFAULT_CHOICE_C))
            .jsonPath("$.[*].choiceD")
            .value(hasItem(DEFAULT_CHOICE_D))
            .jsonPath("$.[*].correctChoice")
            .value(hasItem(DEFAULT_CORRECT_CHOICE))
            .jsonPath("$.[*].correctText")
            .value(hasItem(DEFAULT_CORRECT_TEXT));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsEnabled() {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(questionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsNotEnabled() {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(questionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getQuestion() {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        // Get the question
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, question.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(question.getId().intValue()))
            .jsonPath("$.prompt")
            .value(is(DEFAULT_PROMPT))
            .jsonPath("$.difficulty")
            .value(is(DEFAULT_DIFFICULTY))
            .jsonPath("$.explanation")
            .value(is(DEFAULT_EXPLANATION))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE))
            .jsonPath("$.choiceA")
            .value(is(DEFAULT_CHOICE_A))
            .jsonPath("$.choiceB")
            .value(is(DEFAULT_CHOICE_B))
            .jsonPath("$.choiceC")
            .value(is(DEFAULT_CHOICE_C))
            .jsonPath("$.choiceD")
            .value(is(DEFAULT_CHOICE_D))
            .jsonPath("$.correctChoice")
            .value(is(DEFAULT_CORRECT_CHOICE))
            .jsonPath("$.correctText")
            .value(is(DEFAULT_CORRECT_TEXT));
    }

    @Test
    void getNonExistingQuestion() {
        // Get the question
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).block();
        updatedQuestion
            .prompt(UPDATED_PROMPT)
            .difficulty(UPDATED_DIFFICULTY)
            .explanation(UPDATED_EXPLANATION)
            .active(UPDATED_ACTIVE)
            .choiceA(UPDATED_CHOICE_A)
            .choiceB(UPDATED_CHOICE_B)
            .choiceC(UPDATED_CHOICE_C)
            .choiceD(UPDATED_CHOICE_D)
            .correctChoice(UPDATED_CORRECT_CHOICE)
            .correctText(UPDATED_CORRECT_TEXT);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, questionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionToMatchAllProperties(updatedQuestion);
    }

    @Test
    void putNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, questionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .prompt(UPDATED_PROMPT)
            .choiceB(UPDATED_CHOICE_B)
            .choiceC(UPDATED_CHOICE_C)
            .choiceD(UPDATED_CHOICE_D)
            .correctText(UPDATED_CORRECT_TEXT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuestion, question), getPersistedQuestion(question));
    }

    @Test
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .prompt(UPDATED_PROMPT)
            .difficulty(UPDATED_DIFFICULTY)
            .explanation(UPDATED_EXPLANATION)
            .active(UPDATED_ACTIVE)
            .choiceA(UPDATED_CHOICE_A)
            .choiceB(UPDATED_CHOICE_B)
            .choiceC(UPDATED_CHOICE_C)
            .choiceD(UPDATED_CHOICE_D)
            .correctChoice(UPDATED_CORRECT_CHOICE)
            .correctText(UPDATED_CORRECT_TEXT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(partialUpdatedQuestion, getPersistedQuestion(partialUpdatedQuestion));
    }

    @Test
    void patchNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, questionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQuestion() {
        // Initialize the database
        insertedQuestion = questionRepository.save(question).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the question
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, question.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionRepository.count().block();
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

    protected Question getPersistedQuestion(Question question) {
        return questionRepository.findById(question.getId()).block();
    }

    protected void assertPersistedQuestionToMatchAllProperties(Question expectedQuestion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionAllPropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
        assertQuestionUpdatableFieldsEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }

    protected void assertPersistedQuestionToMatchUpdatableProperties(Question expectedQuestion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionAllUpdatablePropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
        assertQuestionUpdatableFieldsEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }
}
