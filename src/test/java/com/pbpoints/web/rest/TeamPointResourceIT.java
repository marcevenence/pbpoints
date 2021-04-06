package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Team;
import com.pbpoints.domain.TeamPoint;
import com.pbpoints.domain.Tournament;
import com.pbpoints.repository.TeamPointRepository;
import com.pbpoints.service.criteria.TeamPointCriteria;
import com.pbpoints.service.dto.TeamPointDTO;
import com.pbpoints.service.mapper.TeamPointMapper;
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
 * Integration tests for the {@link TeamPointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TeamPointResourceIT {

    private static final Float DEFAULT_POINTS = 1F;
    private static final Float UPDATED_POINTS = 2F;
    private static final Float SMALLER_POINTS = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/team-points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TeamPointRepository teamPointRepository;

    @Autowired
    private TeamPointMapper teamPointMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeamPointMockMvc;

    private TeamPoint teamPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamPoint createEntity(EntityManager em) {
        TeamPoint teamPoint = new TeamPoint().points(DEFAULT_POINTS);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        teamPoint.setTeam(team);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        teamPoint.setTournament(tournament);
        return teamPoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamPoint createUpdatedEntity(EntityManager em) {
        TeamPoint teamPoint = new TeamPoint().points(UPDATED_POINTS);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        teamPoint.setTeam(team);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        teamPoint.setTournament(tournament);
        return teamPoint;
    }

    @BeforeEach
    public void initTest() {
        teamPoint = createEntity(em);
    }

    @Test
    @Transactional
    void createTeamPoint() throws Exception {
        int databaseSizeBeforeCreate = teamPointRepository.findAll().size();
        // Create the TeamPoint
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);
        restTeamPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamPointDTO)))
            .andExpect(status().isCreated());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeCreate + 1);
        TeamPoint testTeamPoint = teamPointList.get(teamPointList.size() - 1);
        assertThat(testTeamPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void createTeamPointWithExistingId() throws Exception {
        // Create the TeamPoint with an existing ID
        teamPoint.setId(1L);
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        int databaseSizeBeforeCreate = teamPointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamPointRepository.findAll().size();
        // set the field null
        teamPoint.setPoints(null);

        // Create the TeamPoint, which fails.
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        restTeamPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamPointDTO)))
            .andExpect(status().isBadRequest());

        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTeamPoints() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList
        restTeamPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));
    }

    @Test
    @Transactional
    void getTeamPoint() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get the teamPoint
        restTeamPointMockMvc
            .perform(get(ENTITY_API_URL_ID, teamPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teamPoint.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()));
    }

    @Test
    @Transactional
    void getTeamPointsByIdFiltering() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        Long id = teamPoint.getId();

        defaultTeamPointShouldBeFound("id.equals=" + id);
        defaultTeamPointShouldNotBeFound("id.notEquals=" + id);

        defaultTeamPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTeamPointShouldNotBeFound("id.greaterThan=" + id);

        defaultTeamPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTeamPointShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTeamPointsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points equals to DEFAULT_POINTS
        defaultTeamPointShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the teamPointList where points equals to UPDATED_POINTS
        defaultTeamPointShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamPointsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points not equals to DEFAULT_POINTS
        defaultTeamPointShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the teamPointList where points not equals to UPDATED_POINTS
        defaultTeamPointShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamPointsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultTeamPointShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the teamPointList where points equals to UPDATED_POINTS
        defaultTeamPointShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamPointsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is not null
        defaultTeamPointShouldBeFound("points.specified=true");

        // Get all the teamPointList where points is null
        defaultTeamPointShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllTeamPointsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is greater than or equal to DEFAULT_POINTS
        defaultTeamPointShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamPointList where points is greater than or equal to UPDATED_POINTS
        defaultTeamPointShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamPointsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is less than or equal to DEFAULT_POINTS
        defaultTeamPointShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamPointList where points is less than or equal to SMALLER_POINTS
        defaultTeamPointShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamPointsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is less than DEFAULT_POINTS
        defaultTeamPointShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the teamPointList where points is less than UPDATED_POINTS
        defaultTeamPointShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamPointsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is greater than DEFAULT_POINTS
        defaultTeamPointShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the teamPointList where points is greater than SMALLER_POINTS
        defaultTeamPointShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllTeamPointsByTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);
        Team team = TeamResourceIT.createEntity(em);
        em.persist(team);
        em.flush();
        teamPoint.setTeam(team);
        teamPointRepository.saveAndFlush(teamPoint);
        Long teamId = team.getId();

        // Get all the teamPointList where team equals to teamId
        defaultTeamPointShouldBeFound("teamId.equals=" + teamId);

        // Get all the teamPointList where team equals to (teamId + 1)
        defaultTeamPointShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }

    @Test
    @Transactional
    void getAllTeamPointsByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        teamPoint.setTournament(tournament);
        teamPointRepository.saveAndFlush(teamPoint);
        Long tournamentId = tournament.getId();

        // Get all the teamPointList where tournament equals to tournamentId
        defaultTeamPointShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the teamPointList where tournament equals to (tournamentId + 1)
        defaultTeamPointShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeamPointShouldBeFound(String filter) throws Exception {
        restTeamPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));

        // Check, that the count call also returns 1
        restTeamPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeamPointShouldNotBeFound(String filter) throws Exception {
        restTeamPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeamPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTeamPoint() throws Exception {
        // Get the teamPoint
        restTeamPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTeamPoint() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();

        // Update the teamPoint
        TeamPoint updatedTeamPoint = teamPointRepository.findById(teamPoint.getId()).get();
        // Disconnect from session so that the updates on updatedTeamPoint are not directly saved in db
        em.detach(updatedTeamPoint);
        updatedTeamPoint.points(UPDATED_POINTS);
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(updatedTeamPoint);

        restTeamPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teamPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamPointDTO))
            )
            .andExpect(status().isOk());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
        TeamPoint testTeamPoint = teamPointList.get(teamPointList.size() - 1);
        assertThat(testTeamPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingTeamPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();
        teamPoint.setId(count.incrementAndGet());

        // Create the TeamPoint
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teamPointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeamPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();
        teamPoint.setId(count.incrementAndGet());

        // Create the TeamPoint
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeamPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();
        teamPoint.setId(count.incrementAndGet());

        // Create the TeamPoint
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamPointMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamPointDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeamPointWithPatch() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();

        // Update the teamPoint using partial update
        TeamPoint partialUpdatedTeamPoint = new TeamPoint();
        partialUpdatedTeamPoint.setId(teamPoint.getId());

        restTeamPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeamPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeamPoint))
            )
            .andExpect(status().isOk());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
        TeamPoint testTeamPoint = teamPointList.get(teamPointList.size() - 1);
        assertThat(testTeamPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void fullUpdateTeamPointWithPatch() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();

        // Update the teamPoint using partial update
        TeamPoint partialUpdatedTeamPoint = new TeamPoint();
        partialUpdatedTeamPoint.setId(teamPoint.getId());

        partialUpdatedTeamPoint.points(UPDATED_POINTS);

        restTeamPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeamPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeamPoint))
            )
            .andExpect(status().isOk());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
        TeamPoint testTeamPoint = teamPointList.get(teamPointList.size() - 1);
        assertThat(testTeamPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingTeamPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();
        teamPoint.setId(count.incrementAndGet());

        // Create the TeamPoint
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teamPointDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeamPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();
        teamPoint.setId(count.incrementAndGet());

        // Create the TeamPoint
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamPointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeamPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();
        teamPoint.setId(count.incrementAndGet());

        // Create the TeamPoint
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamPointMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(teamPointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeamPoint() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        int databaseSizeBeforeDelete = teamPointRepository.findAll().size();

        // Delete the teamPoint
        restTeamPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, teamPoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
