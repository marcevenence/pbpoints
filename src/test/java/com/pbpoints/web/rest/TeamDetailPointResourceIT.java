package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.TeamDetailPoint;
import com.pbpoints.domain.TeamPoint;
import com.pbpoints.repository.TeamDetailPointRepository;
import com.pbpoints.service.criteria.TeamDetailPointCriteria;
import com.pbpoints.service.dto.TeamDetailPointDTO;
import com.pbpoints.service.mapper.TeamDetailPointMapper;
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
 * Integration tests for the {@link TeamDetailPointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TeamDetailPointResourceIT {

    private static final Float DEFAULT_POINTS = 1F;
    private static final Float UPDATED_POINTS = 2F;
    private static final Float SMALLER_POINTS = 1F - 1F;

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;
    private static final Integer SMALLER_POSITION = 1 - 1;

    private static final String ENTITY_API_URL = "/api/team-detail-points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TeamDetailPointRepository teamDetailPointRepository;

    @Autowired
    private TeamDetailPointMapper teamDetailPointMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeamDetailPointMockMvc;

    private TeamDetailPoint teamDetailPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamDetailPoint createEntity(EntityManager em) {
        TeamDetailPoint teamDetailPoint = new TeamDetailPoint().points(DEFAULT_POINTS).position(DEFAULT_POSITION);
        // Add required entity
        TeamPoint teamPoint;
        if (TestUtil.findAll(em, TeamPoint.class).isEmpty()) {
            teamPoint = TeamPointResourceIT.createEntity(em);
            em.persist(teamPoint);
            em.flush();
        } else {
            teamPoint = TestUtil.findAll(em, TeamPoint.class).get(0);
        }
        teamDetailPoint.setTeamPoint(teamPoint);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        teamDetailPoint.setEventCategory(eventCategory);
        return teamDetailPoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamDetailPoint createUpdatedEntity(EntityManager em) {
        TeamDetailPoint teamDetailPoint = new TeamDetailPoint().points(UPDATED_POINTS).position(UPDATED_POSITION);
        // Add required entity
        TeamPoint teamPoint;
        if (TestUtil.findAll(em, TeamPoint.class).isEmpty()) {
            teamPoint = TeamPointResourceIT.createUpdatedEntity(em);
            em.persist(teamPoint);
            em.flush();
        } else {
            teamPoint = TestUtil.findAll(em, TeamPoint.class).get(0);
        }
        teamDetailPoint.setTeamPoint(teamPoint);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createUpdatedEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        teamDetailPoint.setEventCategory(eventCategory);
        return teamDetailPoint;
    }

    @BeforeEach
    public void initTest() {
        teamDetailPoint = createEntity(em);
    }

    @Test
    @Transactional
    void createTeamDetailPoint() throws Exception {
        int databaseSizeBeforeCreate = teamDetailPointRepository.findAll().size();
        // Create the TeamDetailPoint
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);
        restTeamDetailPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeCreate + 1);
        TeamDetailPoint testTeamDetailPoint = teamDetailPointList.get(teamDetailPointList.size() - 1);
        assertThat(testTeamDetailPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testTeamDetailPoint.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void createTeamDetailPointWithExistingId() throws Exception {
        // Create the TeamDetailPoint with an existing ID
        teamDetailPoint.setId(1L);
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        int databaseSizeBeforeCreate = teamDetailPointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamDetailPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamDetailPointRepository.findAll().size();
        // set the field null
        teamDetailPoint.setPoints(null);

        // Create the TeamDetailPoint, which fails.
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        restTeamDetailPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamDetailPointRepository.findAll().size();
        // set the field null
        teamDetailPoint.setPosition(null);

        // Create the TeamDetailPoint, which fails.
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        restTeamDetailPointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTeamDetailPoints() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList
        restTeamDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamDetailPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getTeamDetailPoint() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get the teamDetailPoint
        restTeamDetailPointMockMvc
            .perform(get(ENTITY_API_URL_ID, teamDetailPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teamDetailPoint.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getTeamDetailPointsByIdFiltering() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        Long id = teamDetailPoint.getId();

        defaultTeamDetailPointShouldBeFound("id.equals=" + id);
        defaultTeamDetailPointShouldNotBeFound("id.notEquals=" + id);

        defaultTeamDetailPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTeamDetailPointShouldNotBeFound("id.greaterThan=" + id);

        defaultTeamDetailPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTeamDetailPointShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points equals to DEFAULT_POINTS
        defaultTeamDetailPointShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points equals to UPDATED_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points not equals to DEFAULT_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points not equals to UPDATED_POINTS
        defaultTeamDetailPointShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultTeamDetailPointShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the teamDetailPointList where points equals to UPDATED_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is not null
        defaultTeamDetailPointShouldBeFound("points.specified=true");

        // Get all the teamDetailPointList where points is null
        defaultTeamDetailPointShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is greater than or equal to DEFAULT_POINTS
        defaultTeamDetailPointShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points is greater than or equal to UPDATED_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is less than or equal to DEFAULT_POINTS
        defaultTeamDetailPointShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points is less than or equal to SMALLER_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is less than DEFAULT_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points is less than UPDATED_POINTS
        defaultTeamDetailPointShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is greater than DEFAULT_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points is greater than SMALLER_POINTS
        defaultTeamDetailPointShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where position equals to DEFAULT_POSITION
        defaultTeamDetailPointShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the teamDetailPointList where position equals to UPDATED_POSITION
        defaultTeamDetailPointShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where position not equals to DEFAULT_POSITION
        defaultTeamDetailPointShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the teamDetailPointList where position not equals to UPDATED_POSITION
        defaultTeamDetailPointShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultTeamDetailPointShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the teamDetailPointList where position equals to UPDATED_POSITION
        defaultTeamDetailPointShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where position is not null
        defaultTeamDetailPointShouldBeFound("position.specified=true");

        // Get all the teamDetailPointList where position is null
        defaultTeamDetailPointShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPositionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where position is greater than or equal to DEFAULT_POSITION
        defaultTeamDetailPointShouldBeFound("position.greaterThanOrEqual=" + DEFAULT_POSITION);

        // Get all the teamDetailPointList where position is greater than or equal to UPDATED_POSITION
        defaultTeamDetailPointShouldNotBeFound("position.greaterThanOrEqual=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPositionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where position is less than or equal to DEFAULT_POSITION
        defaultTeamDetailPointShouldBeFound("position.lessThanOrEqual=" + DEFAULT_POSITION);

        // Get all the teamDetailPointList where position is less than or equal to SMALLER_POSITION
        defaultTeamDetailPointShouldNotBeFound("position.lessThanOrEqual=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPositionIsLessThanSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where position is less than DEFAULT_POSITION
        defaultTeamDetailPointShouldNotBeFound("position.lessThan=" + DEFAULT_POSITION);

        // Get all the teamDetailPointList where position is less than UPDATED_POSITION
        defaultTeamDetailPointShouldBeFound("position.lessThan=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByPositionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where position is greater than DEFAULT_POSITION
        defaultTeamDetailPointShouldNotBeFound("position.greaterThan=" + DEFAULT_POSITION);

        // Get all the teamDetailPointList where position is greater than SMALLER_POSITION
        defaultTeamDetailPointShouldBeFound("position.greaterThan=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByTeamPointIsEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);
        TeamPoint teamPoint = TeamPointResourceIT.createEntity(em);
        em.persist(teamPoint);
        em.flush();
        teamDetailPoint.setTeamPoint(teamPoint);
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);
        Long teamPointId = teamPoint.getId();

        // Get all the teamDetailPointList where teamPoint equals to teamPointId
        defaultTeamDetailPointShouldBeFound("teamPointId.equals=" + teamPointId);

        // Get all the teamDetailPointList where teamPoint equals to (teamPointId + 1)
        defaultTeamDetailPointShouldNotBeFound("teamPointId.equals=" + (teamPointId + 1));
    }

    @Test
    @Transactional
    void getAllTeamDetailPointsByEventCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);
        EventCategory eventCategory = EventCategoryResourceIT.createEntity(em);
        em.persist(eventCategory);
        em.flush();
        teamDetailPoint.setEventCategory(eventCategory);
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);
        Long eventCategoryId = eventCategory.getId();

        // Get all the teamDetailPointList where eventCategory equals to eventCategoryId
        defaultTeamDetailPointShouldBeFound("eventCategoryId.equals=" + eventCategoryId);

        // Get all the teamDetailPointList where eventCategory equals to (eventCategoryId + 1)
        defaultTeamDetailPointShouldNotBeFound("eventCategoryId.equals=" + (eventCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeamDetailPointShouldBeFound(String filter) throws Exception {
        restTeamDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamDetailPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restTeamDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeamDetailPointShouldNotBeFound(String filter) throws Exception {
        restTeamDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeamDetailPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTeamDetailPoint() throws Exception {
        // Get the teamDetailPoint
        restTeamDetailPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTeamDetailPoint() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();

        // Update the teamDetailPoint
        TeamDetailPoint updatedTeamDetailPoint = teamDetailPointRepository.findById(teamDetailPoint.getId()).get();
        // Disconnect from session so that the updates on updatedTeamDetailPoint are not directly saved in db
        em.detach(updatedTeamDetailPoint);
        updatedTeamDetailPoint.points(UPDATED_POINTS).position(UPDATED_POSITION);
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(updatedTeamDetailPoint);

        restTeamDetailPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teamDetailPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isOk());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
        TeamDetailPoint testTeamDetailPoint = teamDetailPointList.get(teamDetailPointList.size() - 1);
        assertThat(testTeamDetailPoint.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testTeamDetailPoint.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingTeamDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();
        teamDetailPoint.setId(count.incrementAndGet());

        // Create the TeamDetailPoint
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamDetailPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teamDetailPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeamDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();
        teamDetailPoint.setId(count.incrementAndGet());

        // Create the TeamDetailPoint
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamDetailPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeamDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();
        teamDetailPoint.setId(count.incrementAndGet());

        // Create the TeamDetailPoint
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamDetailPointMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeamDetailPointWithPatch() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();

        // Update the teamDetailPoint using partial update
        TeamDetailPoint partialUpdatedTeamDetailPoint = new TeamDetailPoint();
        partialUpdatedTeamDetailPoint.setId(teamDetailPoint.getId());

        partialUpdatedTeamDetailPoint.points(UPDATED_POINTS).position(UPDATED_POSITION);

        restTeamDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeamDetailPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeamDetailPoint))
            )
            .andExpect(status().isOk());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
        TeamDetailPoint testTeamDetailPoint = teamDetailPointList.get(teamDetailPointList.size() - 1);
        assertThat(testTeamDetailPoint.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testTeamDetailPoint.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateTeamDetailPointWithPatch() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();

        // Update the teamDetailPoint using partial update
        TeamDetailPoint partialUpdatedTeamDetailPoint = new TeamDetailPoint();
        partialUpdatedTeamDetailPoint.setId(teamDetailPoint.getId());

        partialUpdatedTeamDetailPoint.points(UPDATED_POINTS).position(UPDATED_POSITION);

        restTeamDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeamDetailPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeamDetailPoint))
            )
            .andExpect(status().isOk());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
        TeamDetailPoint testTeamDetailPoint = teamDetailPointList.get(teamDetailPointList.size() - 1);
        assertThat(testTeamDetailPoint.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testTeamDetailPoint.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingTeamDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();
        teamDetailPoint.setId(count.incrementAndGet());

        // Create the TeamDetailPoint
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teamDetailPointDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeamDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();
        teamDetailPoint.setId(count.incrementAndGet());

        // Create the TeamDetailPoint
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeamDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();
        teamDetailPoint.setId(count.incrementAndGet());

        // Create the TeamDetailPoint
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamDetailPointMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeamDetailPoint() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        int databaseSizeBeforeDelete = teamDetailPointRepository.findAll().size();

        // Delete the teamDetailPoint
        restTeamDetailPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, teamDetailPoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
