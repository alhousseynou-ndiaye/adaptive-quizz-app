package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReviewReminderAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReviewReminder;
import com.mycompany.myapp.domain.enumeration.ReminderKind;
import com.mycompany.myapp.domain.enumeration.ReminderStatus;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ReviewReminderRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.ReviewReminderService;
import com.mycompany.myapp.service.dto.ReviewReminderDTO;
import com.mycompany.myapp.service.mapper.ReviewReminderMapper;
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
 * Integration tests for the {@link ReviewReminderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReviewReminderResourceIT {

    private static final Instant DEFAULT_DUE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ReminderStatus DEFAULT_STATUS = ReminderStatus.PENDING;
    private static final ReminderStatus UPDATED_STATUS = ReminderStatus.DONE;

    private static final ReminderKind DEFAULT_KIND = ReminderKind.WEEK_1;
    private static final ReminderKind UPDATED_KIND = ReminderKind.WEEK_3;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DONE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DONE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/review-reminders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewReminderRepository reviewReminderRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ReviewReminderRepository reviewReminderRepositoryMock;

    @Autowired
    private ReviewReminderMapper reviewReminderMapper;

    @Mock
    private ReviewReminderService reviewReminderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ReviewReminder reviewReminder;

    private ReviewReminder insertedReviewReminder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewReminder createEntity() {
        return new ReviewReminder()
            .dueAt(DEFAULT_DUE_AT)
            .status(DEFAULT_STATUS)
            .kind(DEFAULT_KIND)
            .createdAt(DEFAULT_CREATED_AT)
            .doneAt(DEFAULT_DONE_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewReminder createUpdatedEntity() {
        return new ReviewReminder()
            .dueAt(UPDATED_DUE_AT)
            .status(UPDATED_STATUS)
            .kind(UPDATED_KIND)
            .createdAt(UPDATED_CREATED_AT)
            .doneAt(UPDATED_DONE_AT);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ReviewReminder.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        reviewReminder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReviewReminder != null) {
            reviewReminderRepository.delete(insertedReviewReminder).block();
            insertedReviewReminder = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createReviewReminder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReviewReminder
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);
        var returnedReviewReminderDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReviewReminderDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ReviewReminder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReviewReminder = reviewReminderMapper.toEntity(returnedReviewReminderDTO);
        assertReviewReminderUpdatableFieldsEquals(returnedReviewReminder, getPersistedReviewReminder(returnedReviewReminder));

        insertedReviewReminder = returnedReviewReminder;
    }

    @Test
    void createReviewReminderWithExistingId() throws Exception {
        // Create the ReviewReminder with an existing ID
        reviewReminder.setId(1L);
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReviewReminder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkDueAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewReminder.setDueAt(null);

        // Create the ReviewReminder, which fails.
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewReminder.setStatus(null);

        // Create the ReviewReminder, which fails.
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkKindIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewReminder.setKind(null);

        // Create the ReviewReminder, which fails.
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reviewReminder.setCreatedAt(null);

        // Create the ReviewReminder, which fails.
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllReviewReminders() {
        // Initialize the database
        insertedReviewReminder = reviewReminderRepository.save(reviewReminder).block();

        // Get all the reviewReminderList
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
            .value(hasItem(reviewReminder.getId().intValue()))
            .jsonPath("$.[*].dueAt")
            .value(hasItem(DEFAULT_DUE_AT.toString()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].kind")
            .value(hasItem(DEFAULT_KIND.toString()))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].doneAt")
            .value(hasItem(DEFAULT_DONE_AT.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewRemindersWithEagerRelationshipsIsEnabled() {
        when(reviewReminderServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(reviewReminderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewRemindersWithEagerRelationshipsIsNotEnabled() {
        when(reviewReminderServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(reviewReminderRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getReviewReminder() {
        // Initialize the database
        insertedReviewReminder = reviewReminderRepository.save(reviewReminder).block();

        // Get the reviewReminder
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reviewReminder.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(reviewReminder.getId().intValue()))
            .jsonPath("$.dueAt")
            .value(is(DEFAULT_DUE_AT.toString()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.kind")
            .value(is(DEFAULT_KIND.toString()))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.doneAt")
            .value(is(DEFAULT_DONE_AT.toString()));
    }

    @Test
    void getNonExistingReviewReminder() {
        // Get the reviewReminder
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReviewReminder() throws Exception {
        // Initialize the database
        insertedReviewReminder = reviewReminderRepository.save(reviewReminder).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewReminder
        ReviewReminder updatedReviewReminder = reviewReminderRepository.findById(reviewReminder.getId()).block();
        updatedReviewReminder
            .dueAt(UPDATED_DUE_AT)
            .status(UPDATED_STATUS)
            .kind(UPDATED_KIND)
            .createdAt(UPDATED_CREATED_AT)
            .doneAt(UPDATED_DONE_AT);
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(updatedReviewReminder);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reviewReminderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReviewReminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewReminderToMatchAllProperties(updatedReviewReminder);
    }

    @Test
    void putNonExistingReviewReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewReminder.setId(longCount.incrementAndGet());

        // Create the ReviewReminder
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reviewReminderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReviewReminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReviewReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewReminder.setId(longCount.incrementAndGet());

        // Create the ReviewReminder
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReviewReminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReviewReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewReminder.setId(longCount.incrementAndGet());

        // Create the ReviewReminder
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReviewReminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReviewReminderWithPatch() throws Exception {
        // Initialize the database
        insertedReviewReminder = reviewReminderRepository.save(reviewReminder).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewReminder using partial update
        ReviewReminder partialUpdatedReviewReminder = new ReviewReminder();
        partialUpdatedReviewReminder.setId(reviewReminder.getId());

        partialUpdatedReviewReminder.status(UPDATED_STATUS).kind(UPDATED_KIND).createdAt(UPDATED_CREATED_AT).doneAt(UPDATED_DONE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReviewReminder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReviewReminder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReviewReminder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewReminderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReviewReminder, reviewReminder),
            getPersistedReviewReminder(reviewReminder)
        );
    }

    @Test
    void fullUpdateReviewReminderWithPatch() throws Exception {
        // Initialize the database
        insertedReviewReminder = reviewReminderRepository.save(reviewReminder).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reviewReminder using partial update
        ReviewReminder partialUpdatedReviewReminder = new ReviewReminder();
        partialUpdatedReviewReminder.setId(reviewReminder.getId());

        partialUpdatedReviewReminder
            .dueAt(UPDATED_DUE_AT)
            .status(UPDATED_STATUS)
            .kind(UPDATED_KIND)
            .createdAt(UPDATED_CREATED_AT)
            .doneAt(UPDATED_DONE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReviewReminder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReviewReminder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReviewReminder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewReminderUpdatableFieldsEquals(partialUpdatedReviewReminder, getPersistedReviewReminder(partialUpdatedReviewReminder));
    }

    @Test
    void patchNonExistingReviewReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewReminder.setId(longCount.incrementAndGet());

        // Create the ReviewReminder
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reviewReminderDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReviewReminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReviewReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewReminder.setId(longCount.incrementAndGet());

        // Create the ReviewReminder
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReviewReminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReviewReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reviewReminder.setId(longCount.incrementAndGet());

        // Create the ReviewReminder
        ReviewReminderDTO reviewReminderDTO = reviewReminderMapper.toDto(reviewReminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reviewReminderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReviewReminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReviewReminder() {
        // Initialize the database
        insertedReviewReminder = reviewReminderRepository.save(reviewReminder).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reviewReminder
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reviewReminder.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewReminderRepository.count().block();
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

    protected ReviewReminder getPersistedReviewReminder(ReviewReminder reviewReminder) {
        return reviewReminderRepository.findById(reviewReminder.getId()).block();
    }

    protected void assertPersistedReviewReminderToMatchAllProperties(ReviewReminder expectedReviewReminder) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReviewReminderAllPropertiesEquals(expectedReviewReminder, getPersistedReviewReminder(expectedReviewReminder));
        assertReviewReminderUpdatableFieldsEquals(expectedReviewReminder, getPersistedReviewReminder(expectedReviewReminder));
    }

    protected void assertPersistedReviewReminderToMatchUpdatableProperties(ReviewReminder expectedReviewReminder) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReviewReminderAllUpdatablePropertiesEquals(expectedReviewReminder, getPersistedReviewReminder(expectedReviewReminder));
        assertReviewReminderUpdatableFieldsEquals(expectedReviewReminder, getPersistedReviewReminder(expectedReviewReminder));
    }
}
