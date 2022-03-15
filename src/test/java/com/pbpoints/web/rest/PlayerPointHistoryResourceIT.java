package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Category;
import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.domain.PlayerPointHistory;
import com.pbpoints.domain.Season;
import com.pbpoints.repository.PlayerPointHistoryRepository;
import com.pbpoints.service.criteria.PlayerPointHistoryCriteria;
import com.pbpoints.service.dto.PlayerPointHistoryDTO;
import com.pbpoints.service.mapper.PlayerPointHistoryMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlayerPointHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayerPointHistoryResourceIT {

    private static final Long DEFAULT_POINTS = 1L;
    private static final Float UPDATED_POINTS = Float.valueOf(2L);
    private static final Long SMALLER_POINTS = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/player-point-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayerPointHistoryRepository playerPointHistoryRepository;

    @Autowired
    private PlayerPointHistoryMapper playerPointHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerPointHistoryMockMvc;

    private PlayerPointHistory playerPointHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerPointHistory createEntity(EntityManager em) {
        PlayerPointHistory playerPointHistory = new PlayerPointHistory().points(Float.valueOf(DEFAULT_POINTS));
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        playerPointHistory.setCategory(category);
        // Add required entity
        Season season;
        if (TestUtil.findAll(em, Season.class).isEmpty()) {
            season = SeasonResourceIT.createEntity(em);
            em.persist(season);
            em.flush();
        } else {
            season = TestUtil.findAll(em, Season.class).get(0);
        }
        playerPointHistory.setSeason(season);
        return playerPointHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerPointHistory createUpdatedEntity(EntityManager em) {
        PlayerPointHistory playerPointHistory = new PlayerPointHistory().points(UPDATED_POINTS);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        playerPointHistory.setCategory(category);
        // Add required entity
        Season season;
        if (TestUtil.findAll(em, Season.class).isEmpty()) {
            season = SeasonResourceIT.createUpdatedEntity(em);
            em.persist(season);
            em.flush();
        } else {
            season = TestUtil.findAll(em, Season.class).get(0);
        }
        playerPointHistory.setSeason(season);
        return playerPointHistory;
    }

    @BeforeEach
    public void initTest() {
        playerPointHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayerPointHistory() throws Exception {
        int databaseSizeBeforeCreate = playerPointHistoryRepository.findAll().size();
        // Create the PlayerPointHistory
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(playerPointHistory);
        restPlayerPointHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerPointHistory testPlayerPointHistory = playerPointHistoryList.get(playerPointHistoryList.size() - 1);
        assertThat(testPlayerPointHistory.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void createPlayerPointHistoryWithExistingId() throws Exception {
        // Create the PlayerPointHistory with an existing ID
        playerPointHistory.setId(1L);
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(playerPointHistory);

        int databaseSizeBeforeCreate = playerPointHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerPointHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerPointHistoryRepository.findAll().size();
        // set the field null
        playerPointHistory.setPoints(null);

        // Create the PlayerPointHistory, which fails.
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(playerPointHistory);

        restPlayerPointHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlayerPointHistories() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get all the playerPointHistoryList
        restPlayerPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerPointHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.intValue())));
    }

    @Test
    @Transactional
    void getPlayerPointHistory() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get the playerPointHistory
        restPlayerPointHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, playerPointHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playerPointHistory.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.intValue()));
    }

    @Test
    @Transactional
    void getPlayerPointHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        Long id = playerPointHistory.getId();

        defaultPlayerPointHistoryShouldBeFound("id.equals=" + id);
        defaultPlayerPointHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultPlayerPointHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlayerPointHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultPlayerPointHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlayerPointHistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get all the playerPointHistoryList where points equals to DEFAULT_POINTS
        defaultPlayerPointHistoryShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the playerPointHistoryList where points equals to UPDATED_POINTS
        defaultPlayerPointHistoryShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get all the playerPointHistoryList where points not equals to DEFAULT_POINTS
        defaultPlayerPointHistoryShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the playerPointHistoryList where points not equals to UPDATED_POINTS
        defaultPlayerPointHistoryShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get all the playerPointHistoryList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultPlayerPointHistoryShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the playerPointHistoryList where points equals to UPDATED_POINTS
        defaultPlayerPointHistoryShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get all the playerPointHistoryList where points is not null
        defaultPlayerPointHistoryShouldBeFound("points.specified=true");

        // Get all the playerPointHistoryList where points is null
        defaultPlayerPointHistoryShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get all the playerPointHistoryList where points is greater than or equal to DEFAULT_POINTS
        defaultPlayerPointHistoryShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerPointHistoryList where points is greater than or equal to UPDATED_POINTS
        defaultPlayerPointHistoryShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get all the playerPointHistoryList where points is less than or equal to DEFAULT_POINTS
        defaultPlayerPointHistoryShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerPointHistoryList where points is less than or equal to SMALLER_POINTS
        defaultPlayerPointHistoryShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get all the playerPointHistoryList where points is less than DEFAULT_POINTS
        defaultPlayerPointHistoryShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the playerPointHistoryList where points is less than UPDATED_POINTS
        defaultPlayerPointHistoryShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        // Get all the playerPointHistoryList where points is greater than DEFAULT_POINTS
        defaultPlayerPointHistoryShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the playerPointHistoryList where points is greater than SMALLER_POINTS
        defaultPlayerPointHistoryShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByPlayerPointIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);
        PlayerPoint playerPoint;
        if (TestUtil.findAll(em, PlayerPoint.class).isEmpty()) {
            playerPoint = PlayerPointResourceIT.createEntity(em);
            em.persist(playerPoint);
            em.flush();
        } else {
            playerPoint = TestUtil.findAll(em, PlayerPoint.class).get(0);
        }
        em.persist(playerPoint);
        em.flush();
        playerPointHistory.setPlayerPoint(playerPoint);
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);
        Long playerPointId = playerPoint.getId();

        // Get all the playerPointHistoryList where playerPoint equals to playerPointId
        defaultPlayerPointHistoryShouldBeFound("playerPointId.equals=" + playerPointId);

        // Get all the playerPointHistoryList where playerPoint equals to (playerPointId + 1)
        defaultPlayerPointHistoryShouldNotBeFound("playerPointId.equals=" + (playerPointId + 1));
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        playerPointHistory.setCategory(category);
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);
        Long categoryId = category.getId();

        // Get all the playerPointHistoryList where category equals to categoryId
        defaultPlayerPointHistoryShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the playerPointHistoryList where category equals to (categoryId + 1)
        defaultPlayerPointHistoryShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllPlayerPointHistoriesBySeasonIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);
        Season season;
        if (TestUtil.findAll(em, Season.class).isEmpty()) {
            season = SeasonResourceIT.createEntity(em);
            em.persist(season);
            em.flush();
        } else {
            season = TestUtil.findAll(em, Season.class).get(0);
        }
        em.persist(season);
        em.flush();
        playerPointHistory.setSeason(season);
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);
        Long seasonId = season.getId();

        // Get all the playerPointHistoryList where season equals to seasonId
        defaultPlayerPointHistoryShouldBeFound("seasonId.equals=" + seasonId);

        // Get all the playerPointHistoryList where season equals to (seasonId + 1)
        defaultPlayerPointHistoryShouldNotBeFound("seasonId.equals=" + (seasonId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlayerPointHistoryShouldBeFound(String filter) throws Exception {
        restPlayerPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerPointHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.intValue())));

        // Check, that the count call also returns 1
        restPlayerPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlayerPointHistoryShouldNotBeFound(String filter) throws Exception {
        restPlayerPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlayerPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlayerPointHistory() throws Exception {
        // Get the playerPointHistory
        restPlayerPointHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlayerPointHistory() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        int databaseSizeBeforeUpdate = playerPointHistoryRepository.findAll().size();

        // Update the playerPointHistory
        PlayerPointHistory updatedPlayerPointHistory = playerPointHistoryRepository.findById(playerPointHistory.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerPointHistory are not directly saved in db
        em.detach(updatedPlayerPointHistory);
        updatedPlayerPointHistory.points(UPDATED_POINTS);
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(updatedPlayerPointHistory);

        restPlayerPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerPointHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeUpdate);
        PlayerPointHistory testPlayerPointHistory = playerPointHistoryList.get(playerPointHistoryList.size() - 1);
        assertThat(testPlayerPointHistory.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingPlayerPointHistory() throws Exception {
        int databaseSizeBeforeUpdate = playerPointHistoryRepository.findAll().size();
        playerPointHistory.setId(count.incrementAndGet());

        // Create the PlayerPointHistory
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(playerPointHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerPointHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayerPointHistory() throws Exception {
        int databaseSizeBeforeUpdate = playerPointHistoryRepository.findAll().size();
        playerPointHistory.setId(count.incrementAndGet());

        // Create the PlayerPointHistory
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(playerPointHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayerPointHistory() throws Exception {
        int databaseSizeBeforeUpdate = playerPointHistoryRepository.findAll().size();
        playerPointHistory.setId(count.incrementAndGet());

        // Create the PlayerPointHistory
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(playerPointHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayerPointHistoryWithPatch() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        int databaseSizeBeforeUpdate = playerPointHistoryRepository.findAll().size();

        // Update the playerPointHistory using partial update
        PlayerPointHistory partialUpdatedPlayerPointHistory = new PlayerPointHistory();
        partialUpdatedPlayerPointHistory.setId(playerPointHistory.getId());

        partialUpdatedPlayerPointHistory.points(UPDATED_POINTS);

        restPlayerPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerPointHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerPointHistory))
            )
            .andExpect(status().isOk());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeUpdate);
        PlayerPointHistory testPlayerPointHistory = playerPointHistoryList.get(playerPointHistoryList.size() - 1);
        assertThat(testPlayerPointHistory.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void fullUpdatePlayerPointHistoryWithPatch() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        int databaseSizeBeforeUpdate = playerPointHistoryRepository.findAll().size();

        // Update the playerPointHistory using partial update
        PlayerPointHistory partialUpdatedPlayerPointHistory = new PlayerPointHistory();
        partialUpdatedPlayerPointHistory.setId(playerPointHistory.getId());

        partialUpdatedPlayerPointHistory.points(UPDATED_POINTS);

        restPlayerPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerPointHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerPointHistory))
            )
            .andExpect(status().isOk());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeUpdate);
        PlayerPointHistory testPlayerPointHistory = playerPointHistoryList.get(playerPointHistoryList.size() - 1);
        assertThat(testPlayerPointHistory.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingPlayerPointHistory() throws Exception {
        int databaseSizeBeforeUpdate = playerPointHistoryRepository.findAll().size();
        playerPointHistory.setId(count.incrementAndGet());

        // Create the PlayerPointHistory
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(playerPointHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playerPointHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayerPointHistory() throws Exception {
        int databaseSizeBeforeUpdate = playerPointHistoryRepository.findAll().size();
        playerPointHistory.setId(count.incrementAndGet());

        // Create the PlayerPointHistory
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(playerPointHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayerPointHistory() throws Exception {
        int databaseSizeBeforeUpdate = playerPointHistoryRepository.findAll().size();
        playerPointHistory.setId(count.incrementAndGet());

        // Create the PlayerPointHistory
        PlayerPointHistoryDTO playerPointHistoryDTO = playerPointHistoryMapper.toDto(playerPointHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerPointHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerPointHistory in the database
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayerPointHistory() throws Exception {
        // Initialize the database
        playerPointHistoryRepository.saveAndFlush(playerPointHistory);

        int databaseSizeBeforeDelete = playerPointHistoryRepository.findAll().size();

        // Delete the playerPointHistory
        restPlayerPointHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, playerPointHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerPointHistory> playerPointHistoryList = playerPointHistoryRepository.findAll();
        assertThat(playerPointHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
