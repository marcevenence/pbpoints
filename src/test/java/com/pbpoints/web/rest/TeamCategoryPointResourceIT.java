package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.TeamCategoryPoint;
import com.pbpoints.domain.TeamDetailPoint;
import com.pbpoints.repository.TeamCategoryPointRepository;
import com.pbpoints.service.criteria.TeamCategoryPointCriteria;
import com.pbpoints.service.dto.TeamCategoryPointDTO;
import com.pbpoints.service.mapper.TeamCategoryPointMapper;
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
 * Integration tests for the {@link TeamCategoryPointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TeamCategoryPointResourceIT {

    private static final Float DEFAULT_POINTS = 1F;
    private static final Float UPDATED_POINTS = 2F;
    private static final Float SMALLER_POINTS = 1F - 1F;

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;
    private static final Integer SMALLER_POSITION = 1 - 1;

    private static final String ENTITY_API_URL = "/api/team-category-points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TeamCategoryPointRepository teamCategoryPointRepository;

    @Autowired
    private TeamCategoryPointMapper teamCategoryPointMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeamCategoryPointMockMvc;

    private TeamCategoryPoint teamCategoryPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamCategoryPoint createEntity(EntityManager em) {
        TeamCategoryPoint teamCategoryPoint = new TeamCategoryPoint().points(DEFAULT_POINTS).position(DEFAULT_POSITION);
        return teamCategoryPoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamCategoryPoint createUpdatedEntity(EntityManager em) {
        TeamCategoryPoint teamCategoryPoint = new TeamCategoryPoint().points(UPDATED_POINTS).position(UPDATED_POSITION);
        return teamCategoryPoint;
    }

    @BeforeEach
    public void initTest() {
        teamCategoryPoint = createEntity(em);
    }

    @Test
    @Transactional
    void createTeamCategoryPoint() throws Exception {
        int databaseSizeBeforeCreate = teamCategoryPointRepository.findAll().size();
        // Create the TeamCategoryPoint
        TeamCategoryPointDTO teamCategoryPointDTO = teamCategoryPointMapper.toDto(teamCategoryPoint);
        restTeamCategoryPointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamCategoryPointDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeCreate + 1);
        TeamCategoryPoint testTeamCategoryPoint = teamCategoryPointList.get(teamCategoryPointList.size() - 1);
        assertThat(testTeamCategoryPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testTeamCategoryPoint.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void createTeamCategoryPointWithExistingId() throws Exception {
        // Create the TeamCategoryPoint with an existing ID
        teamCategoryPoint.setId(1L);
        TeamCategoryPointDTO teamCategoryPointDTO = teamCategoryPointMapper.toDto(teamCategoryPoint);

        int databaseSizeBeforeCreate = teamCategoryPointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamCategoryPointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamCategoryPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPoints() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList
        restTeamCategoryPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamCategoryPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getTeamCategoryPoint() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get the teamCategoryPoint
        restTeamCategoryPointMockMvc
            .perform(get(ENTITY_API_URL_ID, teamCategoryPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teamCategoryPoint.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getTeamCategoryPointsByIdFiltering() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        Long id = teamCategoryPoint.getId();

        defaultTeamCategoryPointShouldBeFound("id.equals=" + id);
        defaultTeamCategoryPointShouldNotBeFound("id.notEquals=" + id);

        defaultTeamCategoryPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTeamCategoryPointShouldNotBeFound("id.greaterThan=" + id);

        defaultTeamCategoryPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTeamCategoryPointShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where points equals to DEFAULT_POINTS
        defaultTeamCategoryPointShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the teamCategoryPointList where points equals to UPDATED_POINTS
        defaultTeamCategoryPointShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where points not equals to DEFAULT_POINTS
        defaultTeamCategoryPointShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the teamCategoryPointList where points not equals to UPDATED_POINTS
        defaultTeamCategoryPointShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultTeamCategoryPointShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the teamCategoryPointList where points equals to UPDATED_POINTS
        defaultTeamCategoryPointShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where points is not null
        defaultTeamCategoryPointShouldBeFound("points.specified=true");

        // Get all the teamCategoryPointList where points is null
        defaultTeamCategoryPointShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where points is greater than or equal to DEFAULT_POINTS
        defaultTeamCategoryPointShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamCategoryPointList where points is greater than or equal to UPDATED_POINTS
        defaultTeamCategoryPointShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where points is less than or equal to DEFAULT_POINTS
        defaultTeamCategoryPointShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamCategoryPointList where points is less than or equal to SMALLER_POINTS
        defaultTeamCategoryPointShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where points is less than DEFAULT_POINTS
        defaultTeamCategoryPointShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the teamCategoryPointList where points is less than UPDATED_POINTS
        defaultTeamCategoryPointShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where points is greater than DEFAULT_POINTS
        defaultTeamCategoryPointShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the teamCategoryPointList where points is greater than SMALLER_POINTS
        defaultTeamCategoryPointShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where position equals to DEFAULT_POSITION
        defaultTeamCategoryPointShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the teamCategoryPointList where position equals to UPDATED_POSITION
        defaultTeamCategoryPointShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where position not equals to DEFAULT_POSITION
        defaultTeamCategoryPointShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the teamCategoryPointList where position not equals to UPDATED_POSITION
        defaultTeamCategoryPointShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultTeamCategoryPointShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the teamCategoryPointList where position equals to UPDATED_POSITION
        defaultTeamCategoryPointShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where position is not null
        defaultTeamCategoryPointShouldBeFound("position.specified=true");

        // Get all the teamCategoryPointList where position is null
        defaultTeamCategoryPointShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPositionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where position is greater than or equal to DEFAULT_POSITION
        defaultTeamCategoryPointShouldBeFound("position.greaterThanOrEqual=" + DEFAULT_POSITION);

        // Get all the teamCategoryPointList where position is greater than or equal to UPDATED_POSITION
        defaultTeamCategoryPointShouldNotBeFound("position.greaterThanOrEqual=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPositionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where position is less than or equal to DEFAULT_POSITION
        defaultTeamCategoryPointShouldBeFound("position.lessThanOrEqual=" + DEFAULT_POSITION);

        // Get all the teamCategoryPointList where position is less than or equal to SMALLER_POSITION
        defaultTeamCategoryPointShouldNotBeFound("position.lessThanOrEqual=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPositionIsLessThanSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where position is less than DEFAULT_POSITION
        defaultTeamCategoryPointShouldNotBeFound("position.lessThan=" + DEFAULT_POSITION);

        // Get all the teamCategoryPointList where position is less than UPDATED_POSITION
        defaultTeamCategoryPointShouldBeFound("position.lessThan=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByPositionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        // Get all the teamCategoryPointList where position is greater than DEFAULT_POSITION
        defaultTeamCategoryPointShouldNotBeFound("position.greaterThan=" + DEFAULT_POSITION);

        // Get all the teamCategoryPointList where position is greater than SMALLER_POSITION
        defaultTeamCategoryPointShouldBeFound("position.greaterThan=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByTeamDetailPointIsEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);
        TeamDetailPoint teamDetailPoint = TeamDetailPointResourceIT.createEntity(em);
        em.persist(teamDetailPoint);
        em.flush();
        teamCategoryPoint.setTeamDetailPoint(teamDetailPoint);
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);
        Long teamDetailPointId = teamDetailPoint.getId();

        // Get all the teamCategoryPointList where teamDetailPoint equals to teamDetailPointId
        defaultTeamCategoryPointShouldBeFound("teamDetailPointId.equals=" + teamDetailPointId);

        // Get all the teamCategoryPointList where teamDetailPoint equals to (teamDetailPointId + 1)
        defaultTeamCategoryPointShouldNotBeFound("teamDetailPointId.equals=" + (teamDetailPointId + 1));
    }

    @Test
    @Transactional
    void getAllTeamCategoryPointsByEventCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);
        EventCategory eventCategory = EventCategoryResourceIT.createEntity(em);
        em.persist(eventCategory);
        em.flush();
        teamCategoryPoint.setEventCategory(eventCategory);
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);
        Long eventCategoryId = eventCategory.getId();

        // Get all the teamCategoryPointList where eventCategory equals to eventCategoryId
        defaultTeamCategoryPointShouldBeFound("eventCategoryId.equals=" + eventCategoryId);

        // Get all the teamCategoryPointList where eventCategory equals to (eventCategoryId + 1)
        defaultTeamCategoryPointShouldNotBeFound("eventCategoryId.equals=" + (eventCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeamCategoryPointShouldBeFound(String filter) throws Exception {
        restTeamCategoryPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamCategoryPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restTeamCategoryPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeamCategoryPointShouldNotBeFound(String filter) throws Exception {
        restTeamCategoryPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeamCategoryPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTeamCategoryPoint() throws Exception {
        // Get the teamCategoryPoint
        restTeamCategoryPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTeamCategoryPoint() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        int databaseSizeBeforeUpdate = teamCategoryPointRepository.findAll().size();

        // Update the teamCategoryPoint
        TeamCategoryPoint updatedTeamCategoryPoint = teamCategoryPointRepository.findById(teamCategoryPoint.getId()).get();
        // Disconnect from session so that the updates on updatedTeamCategoryPoint are not directly saved in db
        em.detach(updatedTeamCategoryPoint);
        updatedTeamCategoryPoint.points(UPDATED_POINTS).position(UPDATED_POSITION);
        TeamCategoryPointDTO teamCategoryPointDTO = teamCategoryPointMapper.toDto(updatedTeamCategoryPoint);

        restTeamCategoryPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teamCategoryPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamCategoryPointDTO))
            )
            .andExpect(status().isOk());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeUpdate);
        TeamCategoryPoint testTeamCategoryPoint = teamCategoryPointList.get(teamCategoryPointList.size() - 1);
        assertThat(testTeamCategoryPoint.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testTeamCategoryPoint.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingTeamCategoryPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamCategoryPointRepository.findAll().size();
        teamCategoryPoint.setId(count.incrementAndGet());

        // Create the TeamCategoryPoint
        TeamCategoryPointDTO teamCategoryPointDTO = teamCategoryPointMapper.toDto(teamCategoryPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamCategoryPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teamCategoryPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamCategoryPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeamCategoryPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamCategoryPointRepository.findAll().size();
        teamCategoryPoint.setId(count.incrementAndGet());

        // Create the TeamCategoryPoint
        TeamCategoryPointDTO teamCategoryPointDTO = teamCategoryPointMapper.toDto(teamCategoryPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamCategoryPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamCategoryPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeamCategoryPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamCategoryPointRepository.findAll().size();
        teamCategoryPoint.setId(count.incrementAndGet());

        // Create the TeamCategoryPoint
        TeamCategoryPointDTO teamCategoryPointDTO = teamCategoryPointMapper.toDto(teamCategoryPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamCategoryPointMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamCategoryPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeamCategoryPointWithPatch() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        int databaseSizeBeforeUpdate = teamCategoryPointRepository.findAll().size();

        // Update the teamCategoryPoint using partial update
        TeamCategoryPoint partialUpdatedTeamCategoryPoint = new TeamCategoryPoint();
        partialUpdatedTeamCategoryPoint.setId(teamCategoryPoint.getId());

        partialUpdatedTeamCategoryPoint.position(UPDATED_POSITION);

        restTeamCategoryPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeamCategoryPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeamCategoryPoint))
            )
            .andExpect(status().isOk());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeUpdate);
        TeamCategoryPoint testTeamCategoryPoint = teamCategoryPointList.get(teamCategoryPointList.size() - 1);
        assertThat(testTeamCategoryPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testTeamCategoryPoint.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateTeamCategoryPointWithPatch() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        int databaseSizeBeforeUpdate = teamCategoryPointRepository.findAll().size();

        // Update the teamCategoryPoint using partial update
        TeamCategoryPoint partialUpdatedTeamCategoryPoint = new TeamCategoryPoint();
        partialUpdatedTeamCategoryPoint.setId(teamCategoryPoint.getId());

        partialUpdatedTeamCategoryPoint.points(UPDATED_POINTS).position(UPDATED_POSITION);

        restTeamCategoryPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeamCategoryPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeamCategoryPoint))
            )
            .andExpect(status().isOk());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeUpdate);
        TeamCategoryPoint testTeamCategoryPoint = teamCategoryPointList.get(teamCategoryPointList.size() - 1);
        assertThat(testTeamCategoryPoint.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testTeamCategoryPoint.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingTeamCategoryPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamCategoryPointRepository.findAll().size();
        teamCategoryPoint.setId(count.incrementAndGet());

        // Create the TeamCategoryPoint
        TeamCategoryPointDTO teamCategoryPointDTO = teamCategoryPointMapper.toDto(teamCategoryPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamCategoryPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teamCategoryPointDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamCategoryPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeamCategoryPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamCategoryPointRepository.findAll().size();
        teamCategoryPoint.setId(count.incrementAndGet());

        // Create the TeamCategoryPoint
        TeamCategoryPointDTO teamCategoryPointDTO = teamCategoryPointMapper.toDto(teamCategoryPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamCategoryPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamCategoryPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeamCategoryPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamCategoryPointRepository.findAll().size();
        teamCategoryPoint.setId(count.incrementAndGet());

        // Create the TeamCategoryPoint
        TeamCategoryPointDTO teamCategoryPointDTO = teamCategoryPointMapper.toDto(teamCategoryPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamCategoryPointMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamCategoryPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeamCategoryPoint in the database
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeamCategoryPoint() throws Exception {
        // Initialize the database
        teamCategoryPointRepository.saveAndFlush(teamCategoryPoint);

        int databaseSizeBeforeDelete = teamCategoryPointRepository.findAll().size();

        // Delete the teamCategoryPoint
        restTeamCategoryPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, teamCategoryPoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TeamCategoryPoint> teamCategoryPointList = teamCategoryPointRepository.findAll();
        assertThat(teamCategoryPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
