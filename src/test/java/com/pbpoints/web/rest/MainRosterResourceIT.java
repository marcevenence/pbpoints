package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.MainRoster;
import com.pbpoints.domain.Team;
import com.pbpoints.domain.UserExtra;
import com.pbpoints.repository.MainRosterRepository;
import com.pbpoints.service.criteria.MainRosterCriteria;
import com.pbpoints.service.dto.MainRosterDTO;
import com.pbpoints.service.mapper.MainRosterMapper;
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
 * Integration tests for the {@link MainRosterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MainRosterResourceIT {

    private static final String ENTITY_API_URL = "/api/main-rosters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MainRosterRepository mainRosterRepository;

    @Autowired
    private MainRosterMapper mainRosterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMainRosterMockMvc;

    private MainRoster mainRoster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MainRoster createEntity(EntityManager em) {
        MainRoster mainRoster = new MainRoster();
        return mainRoster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MainRoster createUpdatedEntity(EntityManager em) {
        MainRoster mainRoster = new MainRoster();
        return mainRoster;
    }

    @BeforeEach
    public void initTest() {
        mainRoster = createEntity(em);
    }

    @Test
    @Transactional
    void createMainRoster() throws Exception {
        int databaseSizeBeforeCreate = mainRosterRepository.findAll().size();
        // Create the MainRoster
        MainRosterDTO mainRosterDTO = mainRosterMapper.toDto(mainRoster);
        restMainRosterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainRosterDTO)))
            .andExpect(status().isCreated());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeCreate + 1);
        MainRoster testMainRoster = mainRosterList.get(mainRosterList.size() - 1);
    }

    @Test
    @Transactional
    void createMainRosterWithExistingId() throws Exception {
        // Create the MainRoster with an existing ID
        mainRoster.setId(1L);
        MainRosterDTO mainRosterDTO = mainRosterMapper.toDto(mainRoster);

        int databaseSizeBeforeCreate = mainRosterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMainRosterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainRosterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMainRosters() throws Exception {
        // Initialize the database
        mainRosterRepository.saveAndFlush(mainRoster);

        // Get all the mainRosterList
        restMainRosterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mainRoster.getId().intValue())));
    }

    @Test
    @Transactional
    void getMainRoster() throws Exception {
        // Initialize the database
        mainRosterRepository.saveAndFlush(mainRoster);

        // Get the mainRoster
        restMainRosterMockMvc
            .perform(get(ENTITY_API_URL_ID, mainRoster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mainRoster.getId().intValue()));
    }

    @Test
    @Transactional
    void getMainRostersByIdFiltering() throws Exception {
        // Initialize the database
        mainRosterRepository.saveAndFlush(mainRoster);

        Long id = mainRoster.getId();

        defaultMainRosterShouldBeFound("id.equals=" + id);
        defaultMainRosterShouldNotBeFound("id.notEquals=" + id);

        defaultMainRosterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMainRosterShouldNotBeFound("id.greaterThan=" + id);

        defaultMainRosterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMainRosterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMainRostersByTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        mainRosterRepository.saveAndFlush(mainRoster);
        Team team = TeamResourceIT.createEntity(em);
        em.persist(team);
        em.flush();
        mainRoster.setTeam(team);
        mainRosterRepository.saveAndFlush(mainRoster);
        Long teamId = team.getId();

        // Get all the mainRosterList where team equals to teamId
        defaultMainRosterShouldBeFound("teamId.equals=" + teamId);

        // Get all the mainRosterList where team equals to (teamId + 1)
        defaultMainRosterShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }

    @Test
    @Transactional
    void getAllMainRostersByUserExtraIsEqualToSomething() throws Exception {
        // Initialize the database
        mainRosterRepository.saveAndFlush(mainRoster);
        UserExtra userExtra = UserExtraResourceIT.createEntity(em);
        em.persist(userExtra);
        em.flush();
        mainRoster.setUserExtra(userExtra);
        mainRosterRepository.saveAndFlush(mainRoster);
        Long userExtraId = userExtra.getId();

        // Get all the mainRosterList where userExtra equals to userExtraId
        defaultMainRosterShouldBeFound("userExtraId.equals=" + userExtraId);

        // Get all the mainRosterList where userExtra equals to (userExtraId + 1)
        defaultMainRosterShouldNotBeFound("userExtraId.equals=" + (userExtraId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMainRosterShouldBeFound(String filter) throws Exception {
        restMainRosterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mainRoster.getId().intValue())));

        // Check, that the count call also returns 1
        restMainRosterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMainRosterShouldNotBeFound(String filter) throws Exception {
        restMainRosterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMainRosterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMainRoster() throws Exception {
        // Get the mainRoster
        restMainRosterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMainRoster() throws Exception {
        // Initialize the database
        mainRosterRepository.saveAndFlush(mainRoster);

        int databaseSizeBeforeUpdate = mainRosterRepository.findAll().size();

        // Update the mainRoster
        MainRoster updatedMainRoster = mainRosterRepository.findById(mainRoster.getId()).get();
        // Disconnect from session so that the updates on updatedMainRoster are not directly saved in db
        em.detach(updatedMainRoster);
        MainRosterDTO mainRosterDTO = mainRosterMapper.toDto(updatedMainRoster);

        restMainRosterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mainRosterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mainRosterDTO))
            )
            .andExpect(status().isOk());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeUpdate);
        MainRoster testMainRoster = mainRosterList.get(mainRosterList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingMainRoster() throws Exception {
        int databaseSizeBeforeUpdate = mainRosterRepository.findAll().size();
        mainRoster.setId(count.incrementAndGet());

        // Create the MainRoster
        MainRosterDTO mainRosterDTO = mainRosterMapper.toDto(mainRoster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMainRosterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mainRosterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mainRosterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMainRoster() throws Exception {
        int databaseSizeBeforeUpdate = mainRosterRepository.findAll().size();
        mainRoster.setId(count.incrementAndGet());

        // Create the MainRoster
        MainRosterDTO mainRosterDTO = mainRosterMapper.toDto(mainRoster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainRosterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mainRosterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMainRoster() throws Exception {
        int databaseSizeBeforeUpdate = mainRosterRepository.findAll().size();
        mainRoster.setId(count.incrementAndGet());

        // Create the MainRoster
        MainRosterDTO mainRosterDTO = mainRosterMapper.toDto(mainRoster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainRosterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainRosterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMainRosterWithPatch() throws Exception {
        // Initialize the database
        mainRosterRepository.saveAndFlush(mainRoster);

        int databaseSizeBeforeUpdate = mainRosterRepository.findAll().size();

        // Update the mainRoster using partial update
        MainRoster partialUpdatedMainRoster = new MainRoster();
        partialUpdatedMainRoster.setId(mainRoster.getId());

        restMainRosterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMainRoster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMainRoster))
            )
            .andExpect(status().isOk());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeUpdate);
        MainRoster testMainRoster = mainRosterList.get(mainRosterList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateMainRosterWithPatch() throws Exception {
        // Initialize the database
        mainRosterRepository.saveAndFlush(mainRoster);

        int databaseSizeBeforeUpdate = mainRosterRepository.findAll().size();

        // Update the mainRoster using partial update
        MainRoster partialUpdatedMainRoster = new MainRoster();
        partialUpdatedMainRoster.setId(mainRoster.getId());

        restMainRosterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMainRoster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMainRoster))
            )
            .andExpect(status().isOk());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeUpdate);
        MainRoster testMainRoster = mainRosterList.get(mainRosterList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingMainRoster() throws Exception {
        int databaseSizeBeforeUpdate = mainRosterRepository.findAll().size();
        mainRoster.setId(count.incrementAndGet());

        // Create the MainRoster
        MainRosterDTO mainRosterDTO = mainRosterMapper.toDto(mainRoster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMainRosterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mainRosterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mainRosterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMainRoster() throws Exception {
        int databaseSizeBeforeUpdate = mainRosterRepository.findAll().size();
        mainRoster.setId(count.incrementAndGet());

        // Create the MainRoster
        MainRosterDTO mainRosterDTO = mainRosterMapper.toDto(mainRoster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainRosterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mainRosterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMainRoster() throws Exception {
        int databaseSizeBeforeUpdate = mainRosterRepository.findAll().size();
        mainRoster.setId(count.incrementAndGet());

        // Create the MainRoster
        MainRosterDTO mainRosterDTO = mainRosterMapper.toDto(mainRoster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainRosterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mainRosterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MainRoster in the database
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMainRoster() throws Exception {
        // Initialize the database
        mainRosterRepository.saveAndFlush(mainRoster);

        int databaseSizeBeforeDelete = mainRosterRepository.findAll().size();

        // Delete the mainRoster
        restMainRosterMockMvc
            .perform(delete(ENTITY_API_URL_ID, mainRoster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MainRoster> mainRosterList = mainRosterRepository.findAll();
        assertThat(mainRosterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
