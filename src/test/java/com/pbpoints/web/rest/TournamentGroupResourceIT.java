package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.TournamentGroup;
import com.pbpoints.repository.TournamentGroupRepository;
import com.pbpoints.service.criteria.TournamentGroupCriteria;
import com.pbpoints.service.dto.TournamentGroupDTO;
import com.pbpoints.service.mapper.TournamentGroupMapper;
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
 * Integration tests for the {@link TournamentGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TournamentGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tournament-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TournamentGroupRepository tournamentGroupRepository;

    @Autowired
    private TournamentGroupMapper tournamentGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTournamentGroupMockMvc;

    private TournamentGroup tournamentGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TournamentGroup createEntity(EntityManager em) {
        TournamentGroup tournamentGroup = new TournamentGroup().name(DEFAULT_NAME);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        tournamentGroup.setTournamentA(tournament);
        // Add required entity
        tournamentGroup.setTournamentB(tournament);
        return tournamentGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TournamentGroup createUpdatedEntity(EntityManager em) {
        TournamentGroup tournamentGroup = new TournamentGroup().name(UPDATED_NAME);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        tournamentGroup.setTournamentA(tournament);
        // Add required entity
        tournamentGroup.setTournamentB(tournament);
        return tournamentGroup;
    }

    @BeforeEach
    public void initTest() {
        tournamentGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createTournamentGroup() throws Exception {
        int databaseSizeBeforeCreate = tournamentGroupRepository.findAll().size();
        // Create the TournamentGroup
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(tournamentGroup);
        restTournamentGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeCreate + 1);
        TournamentGroup testTournamentGroup = tournamentGroupList.get(tournamentGroupList.size() - 1);
        assertThat(testTournamentGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTournamentGroupWithExistingId() throws Exception {
        // Create the TournamentGroup with an existing ID
        tournamentGroup.setId(1L);
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(tournamentGroup);

        int databaseSizeBeforeCreate = tournamentGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTournamentGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tournamentGroupRepository.findAll().size();
        // set the field null
        tournamentGroup.setName(null);

        // Create the TournamentGroup, which fails.
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(tournamentGroup);

        restTournamentGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTournamentGroups() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList
        restTournamentGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournamentGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTournamentGroup() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get the tournamentGroup
        restTournamentGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, tournamentGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tournamentGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTournamentGroupsByIdFiltering() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        Long id = tournamentGroup.getId();

        defaultTournamentGroupShouldBeFound("id.equals=" + id);
        defaultTournamentGroupShouldNotBeFound("id.notEquals=" + id);

        defaultTournamentGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTournamentGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultTournamentGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTournamentGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTournamentGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList where name equals to DEFAULT_NAME
        defaultTournamentGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tournamentGroupList where name equals to UPDATED_NAME
        defaultTournamentGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentGroupsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList where name not equals to DEFAULT_NAME
        defaultTournamentGroupShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tournamentGroupList where name not equals to UPDATED_NAME
        defaultTournamentGroupShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTournamentGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tournamentGroupList where name equals to UPDATED_NAME
        defaultTournamentGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList where name is not null
        defaultTournamentGroupShouldBeFound("name.specified=true");

        // Get all the tournamentGroupList where name is null
        defaultTournamentGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTournamentGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList where name contains DEFAULT_NAME
        defaultTournamentGroupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tournamentGroupList where name contains UPDATED_NAME
        defaultTournamentGroupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList where name does not contain DEFAULT_NAME
        defaultTournamentGroupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tournamentGroupList where name does not contain UPDATED_NAME
        defaultTournamentGroupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentGroupsByTournamentAIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);
        Tournament tournamentA = TournamentResourceIT.createEntity(em);
        em.persist(tournamentA);
        em.flush();
        tournamentGroup.setTournamentA(tournamentA);
        tournamentGroupRepository.saveAndFlush(tournamentGroup);
        Long tournamentAId = tournamentA.getId();

        // Get all the tournamentGroupList where tournamentA equals to tournamentAId
        defaultTournamentGroupShouldBeFound("tournamentAId.equals=" + tournamentAId);

        // Get all the tournamentGroupList where tournamentA equals to (tournamentAId + 1)
        defaultTournamentGroupShouldNotBeFound("tournamentAId.equals=" + (tournamentAId + 1));
    }

    @Test
    @Transactional
    void getAllTournamentGroupsByTournamentBIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);
        Tournament tournamentB = TournamentResourceIT.createEntity(em);
        em.persist(tournamentB);
        em.flush();
        tournamentGroup.setTournamentB(tournamentB);
        tournamentGroupRepository.saveAndFlush(tournamentGroup);
        Long tournamentBId = tournamentB.getId();

        // Get all the tournamentGroupList where tournamentB equals to tournamentBId
        defaultTournamentGroupShouldBeFound("tournamentBId.equals=" + tournamentBId);

        // Get all the tournamentGroupList where tournamentB equals to (tournamentBId + 1)
        defaultTournamentGroupShouldNotBeFound("tournamentBId.equals=" + (tournamentBId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTournamentGroupShouldBeFound(String filter) throws Exception {
        restTournamentGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournamentGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTournamentGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTournamentGroupShouldNotBeFound(String filter) throws Exception {
        restTournamentGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTournamentGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTournamentGroup() throws Exception {
        // Get the tournamentGroup
        restTournamentGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTournamentGroup() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();

        // Update the tournamentGroup
        TournamentGroup updatedTournamentGroup = tournamentGroupRepository.findById(tournamentGroup.getId()).get();
        // Disconnect from session so that the updates on updatedTournamentGroup are not directly saved in db
        em.detach(updatedTournamentGroup);
        updatedTournamentGroup.name(UPDATED_NAME);
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(updatedTournamentGroup);

        restTournamentGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tournamentGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
        TournamentGroup testTournamentGroup = tournamentGroupList.get(tournamentGroupList.size() - 1);
        assertThat(testTournamentGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTournamentGroup() throws Exception {
        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();
        tournamentGroup.setId(count.incrementAndGet());

        // Create the TournamentGroup
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(tournamentGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournamentGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tournamentGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTournamentGroup() throws Exception {
        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();
        tournamentGroup.setId(count.incrementAndGet());

        // Create the TournamentGroup
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(tournamentGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTournamentGroup() throws Exception {
        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();
        tournamentGroup.setId(count.incrementAndGet());

        // Create the TournamentGroup
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(tournamentGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentGroupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTournamentGroupWithPatch() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();

        // Update the tournamentGroup using partial update
        TournamentGroup partialUpdatedTournamentGroup = new TournamentGroup();
        partialUpdatedTournamentGroup.setId(tournamentGroup.getId());

        partialUpdatedTournamentGroup.name(UPDATED_NAME);

        restTournamentGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTournamentGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTournamentGroup))
            )
            .andExpect(status().isOk());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
        TournamentGroup testTournamentGroup = tournamentGroupList.get(tournamentGroupList.size() - 1);
        assertThat(testTournamentGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTournamentGroupWithPatch() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();

        // Update the tournamentGroup using partial update
        TournamentGroup partialUpdatedTournamentGroup = new TournamentGroup();
        partialUpdatedTournamentGroup.setId(tournamentGroup.getId());

        partialUpdatedTournamentGroup.name(UPDATED_NAME);

        restTournamentGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTournamentGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTournamentGroup))
            )
            .andExpect(status().isOk());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
        TournamentGroup testTournamentGroup = tournamentGroupList.get(tournamentGroupList.size() - 1);
        assertThat(testTournamentGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTournamentGroup() throws Exception {
        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();
        tournamentGroup.setId(count.incrementAndGet());

        // Create the TournamentGroup
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(tournamentGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournamentGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tournamentGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTournamentGroup() throws Exception {
        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();
        tournamentGroup.setId(count.incrementAndGet());

        // Create the TournamentGroup
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(tournamentGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTournamentGroup() throws Exception {
        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();
        tournamentGroup.setId(count.incrementAndGet());

        // Create the TournamentGroup
        TournamentGroupDTO tournamentGroupDTO = tournamentGroupMapper.toDto(tournamentGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tournamentGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTournamentGroup() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        int databaseSizeBeforeDelete = tournamentGroupRepository.findAll().size();

        // Delete the tournamentGroup
        restTournamentGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, tournamentGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
