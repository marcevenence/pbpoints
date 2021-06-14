package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Category;
import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.User;
import com.pbpoints.repository.PlayerPointRepository;
import com.pbpoints.service.criteria.PlayerPointCriteria;
import com.pbpoints.service.dto.PlayerPointDTO;
import com.pbpoints.service.mapper.PlayerPointMapper;
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
 * Integration tests for the {@link PlayerPointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayerPointResourceIT {

    private static final Float DEFAULT_POINTS = 1F;
    private static final Float UPDATED_POINTS = 2F;
    private static final Float SMALLER_POINTS = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/player-points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayerPointRepository playerPointRepository;

    @Autowired
    private PlayerPointMapper playerPointMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerPointMockMvc;

    private PlayerPoint playerPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerPoint createEntity(EntityManager em) {
        PlayerPoint playerPoint = new PlayerPoint().points(DEFAULT_POINTS);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        playerPoint.setTournament(tournament);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        playerPoint.setUser(user);
        return playerPoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerPoint createUpdatedEntity(EntityManager em) {
        PlayerPoint playerPoint = new PlayerPoint().points(UPDATED_POINTS);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        playerPoint.setTournament(tournament);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        playerPoint.setUser(user);
        return playerPoint;
    }

    @BeforeEach
    public void initTest() {
        playerPoint = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayerPoint() throws Exception {
        int databaseSizeBeforeCreate = playerPointRepository.findAll().size();
        // Create the PlayerPoint
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);
        restPlayerPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerPointDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerPoint testPlayerPoint = playerPointList.get(playerPointList.size() - 1);
        assertThat(testPlayerPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void createPlayerPointWithExistingId() throws Exception {
        // Create the PlayerPoint with an existing ID
        playerPoint.setId(1L);
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        int databaseSizeBeforeCreate = playerPointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerPointRepository.findAll().size();
        // set the field null
        playerPoint.setPoints(null);

        // Create the PlayerPoint, which fails.
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        restPlayerPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerPointDTO))
            )
            .andExpect(status().isBadRequest());

        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlayerPoints() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList
        restPlayerPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));
    }

    @Test
    @Transactional
    void getPlayerPoint() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get the playerPoint
        restPlayerPointMockMvc
            .perform(get(ENTITY_API_URL_ID, playerPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playerPoint.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()));
    }

    @Test
    @Transactional
    void getPlayerPointsByIdFiltering() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        Long id = playerPoint.getId();

        defaultPlayerPointShouldBeFound("id.equals=" + id);
        defaultPlayerPointShouldNotBeFound("id.notEquals=" + id);

        defaultPlayerPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlayerPointShouldNotBeFound("id.greaterThan=" + id);

        defaultPlayerPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlayerPointShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlayerPointsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points equals to DEFAULT_POINTS
        defaultPlayerPointShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the playerPointList where points equals to UPDATED_POINTS
        defaultPlayerPointShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points not equals to DEFAULT_POINTS
        defaultPlayerPointShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the playerPointList where points not equals to UPDATED_POINTS
        defaultPlayerPointShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultPlayerPointShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the playerPointList where points equals to UPDATED_POINTS
        defaultPlayerPointShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is not null
        defaultPlayerPointShouldBeFound("points.specified=true");

        // Get all the playerPointList where points is null
        defaultPlayerPointShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllPlayerPointsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is greater than or equal to DEFAULT_POINTS
        defaultPlayerPointShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerPointList where points is greater than or equal to UPDATED_POINTS
        defaultPlayerPointShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is less than or equal to DEFAULT_POINTS
        defaultPlayerPointShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerPointList where points is less than or equal to SMALLER_POINTS
        defaultPlayerPointShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is less than DEFAULT_POINTS
        defaultPlayerPointShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the playerPointList where points is less than UPDATED_POINTS
        defaultPlayerPointShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is greater than DEFAULT_POINTS
        defaultPlayerPointShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the playerPointList where points is greater than SMALLER_POINTS
        defaultPlayerPointShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerPointsByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        playerPoint.setTournament(tournament);
        playerPointRepository.saveAndFlush(playerPoint);
        Long tournamentId = tournament.getId();

        // Get all the playerPointList where tournament equals to tournamentId
        defaultPlayerPointShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the playerPointList where tournament equals to (tournamentId + 1)
        defaultPlayerPointShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    @Test
    @Transactional
    void getAllPlayerPointsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        playerPoint.setUser(user);
        playerPointRepository.saveAndFlush(playerPoint);
        Long userId = user.getId();

        // Get all the playerPointList where user equals to userId
        defaultPlayerPointShouldBeFound("userId.equals=" + userId);

        // Get all the playerPointList where user equals to (userId + 1)
        defaultPlayerPointShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllPlayerPointsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        playerPoint.setCategory(category);
        playerPointRepository.saveAndFlush(playerPoint);
        Long categoryId = category.getId();

        // Get all the playerPointList where category equals to categoryId
        defaultPlayerPointShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the playerPointList where category equals to (categoryId + 1)
        defaultPlayerPointShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlayerPointShouldBeFound(String filter) throws Exception {
        restPlayerPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));

        // Check, that the count call also returns 1
        restPlayerPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlayerPointShouldNotBeFound(String filter) throws Exception {
        restPlayerPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlayerPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlayerPoint() throws Exception {
        // Get the playerPoint
        restPlayerPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlayerPoint() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();

        // Update the playerPoint
        PlayerPoint updatedPlayerPoint = playerPointRepository.findById(playerPoint.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerPoint are not directly saved in db
        em.detach(updatedPlayerPoint);
        updatedPlayerPoint.points(UPDATED_POINTS);
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(updatedPlayerPoint);

        restPlayerPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
        PlayerPoint testPlayerPoint = playerPointList.get(playerPointList.size() - 1);
        assertThat(testPlayerPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingPlayerPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();
        playerPoint.setId(count.incrementAndGet());

        // Create the PlayerPoint
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayerPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();
        playerPoint.setId(count.incrementAndGet());

        // Create the PlayerPoint
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayerPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();
        playerPoint.setId(count.incrementAndGet());

        // Create the PlayerPoint
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerPointMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerPointDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayerPointWithPatch() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();

        // Update the playerPoint using partial update
        PlayerPoint partialUpdatedPlayerPoint = new PlayerPoint();
        partialUpdatedPlayerPoint.setId(playerPoint.getId());

        restPlayerPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerPoint))
            )
            .andExpect(status().isOk());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
        PlayerPoint testPlayerPoint = playerPointList.get(playerPointList.size() - 1);
        assertThat(testPlayerPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void fullUpdatePlayerPointWithPatch() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();

        // Update the playerPoint using partial update
        PlayerPoint partialUpdatedPlayerPoint = new PlayerPoint();
        partialUpdatedPlayerPoint.setId(playerPoint.getId());

        partialUpdatedPlayerPoint.points(UPDATED_POINTS);

        restPlayerPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerPoint))
            )
            .andExpect(status().isOk());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
        PlayerPoint testPlayerPoint = playerPointList.get(playerPointList.size() - 1);
        assertThat(testPlayerPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingPlayerPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();
        playerPoint.setId(count.incrementAndGet());

        // Create the PlayerPoint
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playerPointDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayerPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();
        playerPoint.setId(count.incrementAndGet());

        // Create the PlayerPoint
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayerPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();
        playerPoint.setId(count.incrementAndGet());

        // Create the PlayerPoint
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerPointMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(playerPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayerPoint() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        int databaseSizeBeforeDelete = playerPointRepository.findAll().size();

        // Delete the playerPoint
        restPlayerPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, playerPoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
