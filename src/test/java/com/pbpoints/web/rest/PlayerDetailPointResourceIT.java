package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Event;
import com.pbpoints.domain.PlayerDetailPoint;
import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.repository.PlayerDetailPointRepository;
import com.pbpoints.service.criteria.PlayerDetailPointCriteria;
import com.pbpoints.service.dto.PlayerDetailPointDTO;
import com.pbpoints.service.mapper.PlayerDetailPointMapper;
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
 * Integration tests for the {@link PlayerDetailPointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayerDetailPointResourceIT {

    private static final Float DEFAULT_POINTS = 1F;
    private static final Float UPDATED_POINTS = 2F;
    private static final Float SMALLER_POINTS = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/player-detail-points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayerDetailPointRepository playerDetailPointRepository;

    @Autowired
    private PlayerDetailPointMapper playerDetailPointMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerDetailPointMockMvc;

    private PlayerDetailPoint playerDetailPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerDetailPoint createEntity(EntityManager em) {
        PlayerDetailPoint playerDetailPoint = new PlayerDetailPoint().points(DEFAULT_POINTS);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        playerDetailPoint.setEvent(event);
        // Add required entity
        PlayerPoint playerPoint;
        if (TestUtil.findAll(em, PlayerPoint.class).isEmpty()) {
            playerPoint = PlayerPointResourceIT.createEntity(em);
            em.persist(playerPoint);
            em.flush();
        } else {
            playerPoint = TestUtil.findAll(em, PlayerPoint.class).get(0);
        }
        playerDetailPoint.setPlayerPoint(playerPoint);
        return playerDetailPoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerDetailPoint createUpdatedEntity(EntityManager em) {
        PlayerDetailPoint playerDetailPoint = new PlayerDetailPoint().points(UPDATED_POINTS);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        playerDetailPoint.setEvent(event);
        // Add required entity
        PlayerPoint playerPoint;
        if (TestUtil.findAll(em, PlayerPoint.class).isEmpty()) {
            playerPoint = PlayerPointResourceIT.createUpdatedEntity(em);
            em.persist(playerPoint);
            em.flush();
        } else {
            playerPoint = TestUtil.findAll(em, PlayerPoint.class).get(0);
        }
        playerDetailPoint.setPlayerPoint(playerPoint);
        return playerDetailPoint;
    }

    @BeforeEach
    public void initTest() {
        playerDetailPoint = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayerDetailPoint() throws Exception {
        int databaseSizeBeforeCreate = playerDetailPointRepository.findAll().size();
        // Create the PlayerDetailPoint
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);
        restPlayerDetailPointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerDetailPoint testPlayerDetailPoint = playerDetailPointList.get(playerDetailPointList.size() - 1);
        assertThat(testPlayerDetailPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void createPlayerDetailPointWithExistingId() throws Exception {
        // Create the PlayerDetailPoint with an existing ID
        playerDetailPoint.setId(1L);
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        int databaseSizeBeforeCreate = playerDetailPointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerDetailPointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerDetailPointRepository.findAll().size();
        // set the field null
        playerDetailPoint.setPoints(null);

        // Create the PlayerDetailPoint, which fails.
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        restPlayerDetailPointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlayerDetailPoints() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList
        restPlayerDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerDetailPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));
    }

    @Test
    @Transactional
    void getPlayerDetailPoint() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get the playerDetailPoint
        restPlayerDetailPointMockMvc
            .perform(get(ENTITY_API_URL_ID, playerDetailPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playerDetailPoint.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()));
    }

    @Test
    @Transactional
    void getPlayerDetailPointsByIdFiltering() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        Long id = playerDetailPoint.getId();

        defaultPlayerDetailPointShouldBeFound("id.equals=" + id);
        defaultPlayerDetailPointShouldNotBeFound("id.notEquals=" + id);

        defaultPlayerDetailPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlayerDetailPointShouldNotBeFound("id.greaterThan=" + id);

        defaultPlayerDetailPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlayerDetailPointShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points equals to DEFAULT_POINTS
        defaultPlayerDetailPointShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points equals to UPDATED_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points not equals to DEFAULT_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points not equals to UPDATED_POINTS
        defaultPlayerDetailPointShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultPlayerDetailPointShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the playerDetailPointList where points equals to UPDATED_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is not null
        defaultPlayerDetailPointShouldBeFound("points.specified=true");

        // Get all the playerDetailPointList where points is null
        defaultPlayerDetailPointShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is greater than or equal to DEFAULT_POINTS
        defaultPlayerDetailPointShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points is greater than or equal to UPDATED_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is less than or equal to DEFAULT_POINTS
        defaultPlayerDetailPointShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points is less than or equal to SMALLER_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is less than DEFAULT_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points is less than UPDATED_POINTS
        defaultPlayerDetailPointShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is greater than DEFAULT_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points is greater than SMALLER_POINTS
        defaultPlayerDetailPointShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);
        Event event = EventResourceIT.createEntity(em);
        em.persist(event);
        em.flush();
        playerDetailPoint.setEvent(event);
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);
        Long eventId = event.getId();

        // Get all the playerDetailPointList where event equals to eventId
        defaultPlayerDetailPointShouldBeFound("eventId.equals=" + eventId);

        // Get all the playerDetailPointList where event equals to (eventId + 1)
        defaultPlayerDetailPointShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllPlayerDetailPointsByPlayerPointIsEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);
        PlayerPoint playerPoint = PlayerPointResourceIT.createEntity(em);
        em.persist(playerPoint);
        em.flush();
        playerDetailPoint.setPlayerPoint(playerPoint);
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);
        Long playerPointId = playerPoint.getId();

        // Get all the playerDetailPointList where playerPoint equals to playerPointId
        defaultPlayerDetailPointShouldBeFound("playerPointId.equals=" + playerPointId);

        // Get all the playerDetailPointList where playerPoint equals to (playerPointId + 1)
        defaultPlayerDetailPointShouldNotBeFound("playerPointId.equals=" + (playerPointId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlayerDetailPointShouldBeFound(String filter) throws Exception {
        restPlayerDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerDetailPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));

        // Check, that the count call also returns 1
        restPlayerDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlayerDetailPointShouldNotBeFound(String filter) throws Exception {
        restPlayerDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlayerDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlayerDetailPoint() throws Exception {
        // Get the playerDetailPoint
        restPlayerDetailPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlayerDetailPoint() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();

        // Update the playerDetailPoint
        PlayerDetailPoint updatedPlayerDetailPoint = playerDetailPointRepository.findById(playerDetailPoint.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerDetailPoint are not directly saved in db
        em.detach(updatedPlayerDetailPoint);
        updatedPlayerDetailPoint.points(UPDATED_POINTS);
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(updatedPlayerDetailPoint);

        restPlayerDetailPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerDetailPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
        PlayerDetailPoint testPlayerDetailPoint = playerDetailPointList.get(playerDetailPointList.size() - 1);
        assertThat(testPlayerDetailPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingPlayerDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();
        playerDetailPoint.setId(count.incrementAndGet());

        // Create the PlayerDetailPoint
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerDetailPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerDetailPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayerDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();
        playerDetailPoint.setId(count.incrementAndGet());

        // Create the PlayerDetailPoint
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerDetailPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayerDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();
        playerDetailPoint.setId(count.incrementAndGet());

        // Create the PlayerDetailPoint
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerDetailPointMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayerDetailPointWithPatch() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();

        // Update the playerDetailPoint using partial update
        PlayerDetailPoint partialUpdatedPlayerDetailPoint = new PlayerDetailPoint();
        partialUpdatedPlayerDetailPoint.setId(playerDetailPoint.getId());

        partialUpdatedPlayerDetailPoint.points(UPDATED_POINTS);

        restPlayerDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerDetailPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerDetailPoint))
            )
            .andExpect(status().isOk());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
        PlayerDetailPoint testPlayerDetailPoint = playerDetailPointList.get(playerDetailPointList.size() - 1);
        assertThat(testPlayerDetailPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void fullUpdatePlayerDetailPointWithPatch() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();

        // Update the playerDetailPoint using partial update
        PlayerDetailPoint partialUpdatedPlayerDetailPoint = new PlayerDetailPoint();
        partialUpdatedPlayerDetailPoint.setId(playerDetailPoint.getId());

        partialUpdatedPlayerDetailPoint.points(UPDATED_POINTS);

        restPlayerDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerDetailPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerDetailPoint))
            )
            .andExpect(status().isOk());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
        PlayerDetailPoint testPlayerDetailPoint = playerDetailPointList.get(playerDetailPointList.size() - 1);
        assertThat(testPlayerDetailPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingPlayerDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();
        playerDetailPoint.setId(count.incrementAndGet());

        // Create the PlayerDetailPoint
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playerDetailPointDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayerDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();
        playerDetailPoint.setId(count.incrementAndGet());

        // Create the PlayerDetailPoint
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayerDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();
        playerDetailPoint.setId(count.incrementAndGet());

        // Create the PlayerDetailPoint
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayerDetailPoint() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        int databaseSizeBeforeDelete = playerDetailPointRepository.findAll().size();

        // Delete the playerDetailPoint
        restPlayerDetailPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, playerDetailPoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
