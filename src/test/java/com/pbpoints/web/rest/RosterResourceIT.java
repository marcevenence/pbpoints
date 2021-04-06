package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Player;
import com.pbpoints.domain.Roster;
import com.pbpoints.domain.Team;
import com.pbpoints.repository.RosterRepository;
import com.pbpoints.service.criteria.RosterCriteria;
import com.pbpoints.service.dto.RosterDTO;
import com.pbpoints.service.mapper.RosterMapper;
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
 * Integration tests for the {@link RosterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RosterResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/rosters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RosterRepository rosterRepository;

    @Autowired
    private RosterMapper rosterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRosterMockMvc;

    private Roster roster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roster createEntity(EntityManager em) {
        Roster roster = new Roster().active(DEFAULT_ACTIVE);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        roster.setTeam(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        roster.setEventCategory(eventCategory);
        return roster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roster createUpdatedEntity(EntityManager em) {
        Roster roster = new Roster().active(UPDATED_ACTIVE);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        roster.setTeam(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createUpdatedEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        roster.setEventCategory(eventCategory);
        return roster;
    }

    @BeforeEach
    public void initTest() {
        roster = createEntity(em);
    }

    @Test
    @Transactional
    void createRoster() throws Exception {
        int databaseSizeBeforeCreate = rosterRepository.findAll().size();
        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);
        restRosterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isCreated());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeCreate + 1);
        Roster testRoster = rosterList.get(rosterList.size() - 1);
        assertThat(testRoster.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createRosterWithExistingId() throws Exception {
        // Create the Roster with an existing ID
        roster.setId(1L);
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        int databaseSizeBeforeCreate = rosterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRosterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRosters() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList
        restRosterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roster.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getRoster() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get the roster
        restRosterMockMvc
            .perform(get(ENTITY_API_URL_ID, roster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roster.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getRostersByIdFiltering() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        Long id = roster.getId();

        defaultRosterShouldBeFound("id.equals=" + id);
        defaultRosterShouldNotBeFound("id.notEquals=" + id);

        defaultRosterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRosterShouldNotBeFound("id.greaterThan=" + id);

        defaultRosterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRosterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRostersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active equals to DEFAULT_ACTIVE
        defaultRosterShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the rosterList where active equals to UPDATED_ACTIVE
        defaultRosterShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRostersByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active not equals to DEFAULT_ACTIVE
        defaultRosterShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the rosterList where active not equals to UPDATED_ACTIVE
        defaultRosterShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRostersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultRosterShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the rosterList where active equals to UPDATED_ACTIVE
        defaultRosterShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRostersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active is not null
        defaultRosterShouldBeFound("active.specified=true");

        // Get all the rosterList where active is null
        defaultRosterShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllRostersByPlayerIsEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);
        Player player = PlayerResourceIT.createEntity(em);
        em.persist(player);
        em.flush();
        roster.addPlayer(player);
        rosterRepository.saveAndFlush(roster);
        Long playerId = player.getId();

        // Get all the rosterList where player equals to playerId
        defaultRosterShouldBeFound("playerId.equals=" + playerId);

        // Get all the rosterList where player equals to (playerId + 1)
        defaultRosterShouldNotBeFound("playerId.equals=" + (playerId + 1));
    }

    @Test
    @Transactional
    void getAllRostersByTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);
        Team team = TeamResourceIT.createEntity(em);
        em.persist(team);
        em.flush();
        roster.setTeam(team);
        rosterRepository.saveAndFlush(roster);
        Long teamId = team.getId();

        // Get all the rosterList where team equals to teamId
        defaultRosterShouldBeFound("teamId.equals=" + teamId);

        // Get all the rosterList where team equals to (teamId + 1)
        defaultRosterShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }

    @Test
    @Transactional
    void getAllRostersByEventCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);
        EventCategory eventCategory = EventCategoryResourceIT.createEntity(em);
        em.persist(eventCategory);
        em.flush();
        roster.setEventCategory(eventCategory);
        rosterRepository.saveAndFlush(roster);
        Long eventCategoryId = eventCategory.getId();

        // Get all the rosterList where eventCategory equals to eventCategoryId
        defaultRosterShouldBeFound("eventCategoryId.equals=" + eventCategoryId);

        // Get all the rosterList where eventCategory equals to (eventCategoryId + 1)
        defaultRosterShouldNotBeFound("eventCategoryId.equals=" + (eventCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRosterShouldBeFound(String filter) throws Exception {
        restRosterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roster.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restRosterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRosterShouldNotBeFound(String filter) throws Exception {
        restRosterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRosterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoster() throws Exception {
        // Get the roster
        restRosterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoster() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();

        // Update the roster
        Roster updatedRoster = rosterRepository.findById(roster.getId()).get();
        // Disconnect from session so that the updates on updatedRoster are not directly saved in db
        em.detach(updatedRoster);
        updatedRoster.active(UPDATED_ACTIVE);
        RosterDTO rosterDTO = rosterMapper.toDto(updatedRoster);

        restRosterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rosterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rosterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
        Roster testRoster = rosterList.get(rosterList.size() - 1);
        assertThat(testRoster.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingRoster() throws Exception {
        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();
        roster.setId(count.incrementAndGet());

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRosterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rosterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rosterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoster() throws Exception {
        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();
        roster.setId(count.incrementAndGet());

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRosterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rosterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoster() throws Exception {
        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();
        roster.setId(count.incrementAndGet());

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRosterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRosterWithPatch() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();

        // Update the roster using partial update
        Roster partialUpdatedRoster = new Roster();
        partialUpdatedRoster.setId(roster.getId());

        partialUpdatedRoster.active(UPDATED_ACTIVE);

        restRosterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoster))
            )
            .andExpect(status().isOk());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
        Roster testRoster = rosterList.get(rosterList.size() - 1);
        assertThat(testRoster.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateRosterWithPatch() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();

        // Update the roster using partial update
        Roster partialUpdatedRoster = new Roster();
        partialUpdatedRoster.setId(roster.getId());

        partialUpdatedRoster.active(UPDATED_ACTIVE);

        restRosterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoster))
            )
            .andExpect(status().isOk());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
        Roster testRoster = rosterList.get(rosterList.size() - 1);
        assertThat(testRoster.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingRoster() throws Exception {
        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();
        roster.setId(count.incrementAndGet());

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRosterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rosterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rosterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoster() throws Exception {
        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();
        roster.setId(count.incrementAndGet());

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRosterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rosterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoster() throws Exception {
        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();
        roster.setId(count.incrementAndGet());

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRosterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rosterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoster() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        int databaseSizeBeforeDelete = rosterRepository.findAll().size();

        // Delete the roster
        restRosterMockMvc
            .perform(delete(ENTITY_API_URL_ID, roster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
