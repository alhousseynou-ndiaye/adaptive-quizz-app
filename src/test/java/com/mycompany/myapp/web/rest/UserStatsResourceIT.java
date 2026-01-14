package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UserStatsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserStats;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.UserStatsRepository;
import com.mycompany.myapp.service.UserStatsService;
import com.mycompany.myapp.service.dto.UserStatsDTO;
import com.mycompany.myapp.service.mapper.UserStatsMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
 * Integration tests for the {@link UserStatsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserStatsResourceIT {

    private static final Integer DEFAULT_CURRENT_DIFFICULTY = 1;
    private static final Integer UPDATED_CURRENT_DIFFICULTY = 2;

    private static final Integer DEFAULT_TOTAL_ANSWERED = 0;
    private static final Integer UPDATED_TOTAL_ANSWERED = 1;

    private static final Integer DEFAULT_TOTAL_CORRECT = 0;
    private static final Integer UPDATED_TOTAL_CORRECT = 1;

    private static final Integer DEFAULT_STREAK_DAYS = 0;
    private static final Integer UPDATED_STREAK_DAYS = 1;

    private static final Instant DEFAULT_LAST_ACTIVE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACTIVE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-stats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserStatsRepository userStatsRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserStatsRepository userStatsRepositoryMock;

    @Autowired
    private UserStatsMapper userStatsMapper;

    @Mock
    private UserStatsService userStatsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserStats userStats;

    private UserStats insertedUserStats;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserStats createEntity() {
        return new UserStats()
            .currentDifficulty(DEFAULT_CURRENT_DIFFICULTY)
            .totalAnswered(DEFAULT_TOTAL_ANSWERED)
            .totalCorrect(DEFAULT_TOTAL_CORRECT)
            .streakDays(DEFAULT_STREAK_DAYS)
            .lastActiveAt(DEFAULT_LAST_ACTIVE_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserStats createUpdatedEntity() {
        return new UserStats()
            .currentDifficulty(UPDATED_CURRENT_DIFFICULTY)
            .totalAnswered(UPDATED_TOTAL_ANSWERED)
            .totalCorrect(UPDATED_TOTAL_CORRECT)
            .streakDays(UPDATED_STREAK_DAYS)
            .lastActiveAt(UPDATED_LAST_ACTIVE_AT);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserStats.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    void initTest() {
        userStats = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUserStats != null) {
            userStatsRepository.delete(insertedUserStats).block();
            insertedUserStats = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createUserStats() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserStats
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);
        var returnedUserStatsDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UserStatsDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the UserStats in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserStats = userStatsMapper.toEntity(returnedUserStatsDTO);
        assertUserStatsUpdatableFieldsEquals(returnedUserStats, getPersistedUserStats(returnedUserStats));

        insertedUserStats = returnedUserStats;
    }

    @Test
    void createUserStatsWithExistingId() throws Exception {
        // Create the UserStats with an existing ID
        userStats.setId(1L);
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserStats in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCurrentDifficultyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userStats.setCurrentDifficulty(null);

        // Create the UserStats, which fails.
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalAnsweredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userStats.setTotalAnswered(null);

        // Create the UserStats, which fails.
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalCorrectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userStats.setTotalCorrect(null);

        // Create the UserStats, which fails.
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkStreakDaysIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userStats.setStreakDays(null);

        // Create the UserStats, which fails.
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserStatsAsStream() {
        // Initialize the database
        userStatsRepository.save(userStats).block();

        List<UserStats> userStatsList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UserStatsDTO.class)
            .getResponseBody()
            .map(userStatsMapper::toEntity)
            .filter(userStats::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(userStatsList).isNotNull();
        assertThat(userStatsList).hasSize(1);
        UserStats testUserStats = userStatsList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertUserStatsAllPropertiesEquals(userStats, testUserStats);
        assertUserStatsUpdatableFieldsEquals(userStats, testUserStats);
    }

    @Test
    void getAllUserStats() {
        // Initialize the database
        insertedUserStats = userStatsRepository.save(userStats).block();

        // Get all the userStatsList
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
            .value(hasItem(userStats.getId().intValue()))
            .jsonPath("$.[*].currentDifficulty")
            .value(hasItem(DEFAULT_CURRENT_DIFFICULTY))
            .jsonPath("$.[*].totalAnswered")
            .value(hasItem(DEFAULT_TOTAL_ANSWERED))
            .jsonPath("$.[*].totalCorrect")
            .value(hasItem(DEFAULT_TOTAL_CORRECT))
            .jsonPath("$.[*].streakDays")
            .value(hasItem(DEFAULT_STREAK_DAYS))
            .jsonPath("$.[*].lastActiveAt")
            .value(hasItem(DEFAULT_LAST_ACTIVE_AT.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserStatsWithEagerRelationshipsIsEnabled() {
        when(userStatsServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(userStatsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserStatsWithEagerRelationshipsIsNotEnabled() {
        when(userStatsServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(userStatsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getUserStats() {
        // Initialize the database
        insertedUserStats = userStatsRepository.save(userStats).block();

        // Get the userStats
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userStats.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userStats.getId().intValue()))
            .jsonPath("$.currentDifficulty")
            .value(is(DEFAULT_CURRENT_DIFFICULTY))
            .jsonPath("$.totalAnswered")
            .value(is(DEFAULT_TOTAL_ANSWERED))
            .jsonPath("$.totalCorrect")
            .value(is(DEFAULT_TOTAL_CORRECT))
            .jsonPath("$.streakDays")
            .value(is(DEFAULT_STREAK_DAYS))
            .jsonPath("$.lastActiveAt")
            .value(is(DEFAULT_LAST_ACTIVE_AT.toString()));
    }

    @Test
    void getNonExistingUserStats() {
        // Get the userStats
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserStats() throws Exception {
        // Initialize the database
        insertedUserStats = userStatsRepository.save(userStats).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userStats
        UserStats updatedUserStats = userStatsRepository.findById(userStats.getId()).block();
        updatedUserStats
            .currentDifficulty(UPDATED_CURRENT_DIFFICULTY)
            .totalAnswered(UPDATED_TOTAL_ANSWERED)
            .totalCorrect(UPDATED_TOTAL_CORRECT)
            .streakDays(UPDATED_STREAK_DAYS)
            .lastActiveAt(UPDATED_LAST_ACTIVE_AT);
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(updatedUserStats);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userStatsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserStatsToMatchAllProperties(updatedUserStats);
    }

    @Test
    void putNonExistingUserStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStats.setId(longCount.incrementAndGet());

        // Create the UserStats
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userStatsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStats.setId(longCount.incrementAndGet());

        // Create the UserStats
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStats.setId(longCount.incrementAndGet());

        // Create the UserStats
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserStatsWithPatch() throws Exception {
        // Initialize the database
        insertedUserStats = userStatsRepository.save(userStats).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userStats using partial update
        UserStats partialUpdatedUserStats = new UserStats();
        partialUpdatedUserStats.setId(userStats.getId());

        partialUpdatedUserStats.currentDifficulty(UPDATED_CURRENT_DIFFICULTY).lastActiveAt(UPDATED_LAST_ACTIVE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserStats.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserStats))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserStats in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserStatsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserStats, userStats),
            getPersistedUserStats(userStats)
        );
    }

    @Test
    void fullUpdateUserStatsWithPatch() throws Exception {
        // Initialize the database
        insertedUserStats = userStatsRepository.save(userStats).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userStats using partial update
        UserStats partialUpdatedUserStats = new UserStats();
        partialUpdatedUserStats.setId(userStats.getId());

        partialUpdatedUserStats
            .currentDifficulty(UPDATED_CURRENT_DIFFICULTY)
            .totalAnswered(UPDATED_TOTAL_ANSWERED)
            .totalCorrect(UPDATED_TOTAL_CORRECT)
            .streakDays(UPDATED_STREAK_DAYS)
            .lastActiveAt(UPDATED_LAST_ACTIVE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserStats.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserStats))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserStats in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserStatsUpdatableFieldsEquals(partialUpdatedUserStats, getPersistedUserStats(partialUpdatedUserStats));
    }

    @Test
    void patchNonExistingUserStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStats.setId(longCount.incrementAndGet());

        // Create the UserStats
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userStatsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStats.setId(longCount.incrementAndGet());

        // Create the UserStats
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userStats.setId(longCount.incrementAndGet());

        // Create the UserStats
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userStatsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserStats() {
        // Initialize the database
        insertedUserStats = userStatsRepository.save(userStats).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userStats
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userStats.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userStatsRepository.count().block();
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

    protected UserStats getPersistedUserStats(UserStats userStats) {
        return userStatsRepository.findById(userStats.getId()).block();
    }

    protected void assertPersistedUserStatsToMatchAllProperties(UserStats expectedUserStats) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserStatsAllPropertiesEquals(expectedUserStats, getPersistedUserStats(expectedUserStats));
        assertUserStatsUpdatableFieldsEquals(expectedUserStats, getPersistedUserStats(expectedUserStats));
    }

    protected void assertPersistedUserStatsToMatchUpdatableProperties(UserStats expectedUserStats) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserStatsAllUpdatablePropertiesEquals(expectedUserStats, getPersistedUserStats(expectedUserStats));
        assertUserStatsUpdatableFieldsEquals(expectedUserStats, getPersistedUserStats(expectedUserStats));
    }
}
