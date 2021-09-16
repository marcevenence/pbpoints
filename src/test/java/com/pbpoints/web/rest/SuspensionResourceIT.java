package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Suspension;
import com.pbpoints.domain.User;
import com.pbpoints.repository.SuspensionRepository;
import com.pbpoints.service.criteria.SuspensionCriteria;
import com.pbpoints.service.dto.SuspensionDTO;
import com.pbpoints.service.mapper.SuspensionMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SuspensionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuspensionResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/suspensions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SuspensionRepository suspensionRepository;

    @Autowired
    private SuspensionMapper suspensionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuspensionMockMvc;

    private Suspension suspension;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suspension createEntity(EntityManager em) {
        Suspension suspension = new Suspension().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        suspension.setUser(user);
        return suspension;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suspension createUpdatedEntity(EntityManager em) {
        Suspension suspension = new Suspension().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        suspension.setUser(user);
        return suspension;
    }

    @BeforeEach
    public void initTest() {
        suspension = createEntity(em);
    }

    @Test
    @Transactional
    void createSuspension() throws Exception {
        int databaseSizeBeforeCreate = suspensionRepository.findAll().size();
        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);
        restSuspensionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(suspensionDTO)))
            .andExpect(status().isCreated());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeCreate + 1);
        Suspension testSuspension = suspensionList.get(suspensionList.size() - 1);
        assertThat(testSuspension.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSuspension.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the id for MapsId, the ids must be same
        assertThat(testSuspension.getId()).isEqualTo(testSuspension.getUser().getId());
    }

    @Test
    @Transactional
    void createSuspensionWithExistingId() throws Exception {
        // Create the Suspension with an existing ID
        suspension.setId(1L);
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        int databaseSizeBeforeCreate = suspensionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuspensionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(suspensionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateSuspensionMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);
        int databaseSizeBeforeCreate = suspensionRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the suspension
        Suspension updatedSuspension = suspensionRepository.findById(suspension.getId()).get();
        assertThat(updatedSuspension).isNotNull();
        // Disconnect from session so that the updates on updatedSuspension are not directly saved in db
        em.detach(updatedSuspension);

        // Update the User with new association value
        updatedSuspension.setUser(user);
        SuspensionDTO updatedSuspensionDTO = suspensionMapper.toDto(updatedSuspension);
        assertThat(updatedSuspensionDTO).isNotNull();

        // Update the entity
        restSuspensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSuspensionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSuspensionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeCreate);
        Suspension testSuspension = suspensionList.get(suspensionList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testSuspension.getId()).isEqualTo(testSuspension.getUser().getId());
    }

    @Test
    @Transactional
    void getAllSuspensions() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList
        restSuspensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suspension.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getSuspension() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get the suspension
        restSuspensionMockMvc
            .perform(get(ENTITY_API_URL_ID, suspension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(suspension.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getSuspensionsByIdFiltering() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        Long id = suspension.getId();

        defaultSuspensionShouldBeFound("id.equals=" + id);
        defaultSuspensionShouldNotBeFound("id.notEquals=" + id);

        defaultSuspensionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSuspensionShouldNotBeFound("id.greaterThan=" + id);

        defaultSuspensionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSuspensionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuspensionsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where startDate equals to DEFAULT_START_DATE
        defaultSuspensionShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the suspensionList where startDate equals to UPDATED_START_DATE
        defaultSuspensionShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where startDate not equals to DEFAULT_START_DATE
        defaultSuspensionShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the suspensionList where startDate not equals to UPDATED_START_DATE
        defaultSuspensionShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultSuspensionShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the suspensionList where startDate equals to UPDATED_START_DATE
        defaultSuspensionShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where startDate is not null
        defaultSuspensionShouldBeFound("startDate.specified=true");

        // Get all the suspensionList where startDate is null
        defaultSuspensionShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSuspensionsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultSuspensionShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the suspensionList where startDate is greater than or equal to UPDATED_START_DATE
        defaultSuspensionShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where startDate is less than or equal to DEFAULT_START_DATE
        defaultSuspensionShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the suspensionList where startDate is less than or equal to SMALLER_START_DATE
        defaultSuspensionShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where startDate is less than DEFAULT_START_DATE
        defaultSuspensionShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the suspensionList where startDate is less than UPDATED_START_DATE
        defaultSuspensionShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where startDate is greater than DEFAULT_START_DATE
        defaultSuspensionShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the suspensionList where startDate is greater than SMALLER_START_DATE
        defaultSuspensionShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where endDate equals to DEFAULT_END_DATE
        defaultSuspensionShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the suspensionList where endDate equals to UPDATED_END_DATE
        defaultSuspensionShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where endDate not equals to DEFAULT_END_DATE
        defaultSuspensionShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the suspensionList where endDate not equals to UPDATED_END_DATE
        defaultSuspensionShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultSuspensionShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the suspensionList where endDate equals to UPDATED_END_DATE
        defaultSuspensionShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where endDate is not null
        defaultSuspensionShouldBeFound("endDate.specified=true");

        // Get all the suspensionList where endDate is null
        defaultSuspensionShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSuspensionsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultSuspensionShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the suspensionList where endDate is greater than or equal to UPDATED_END_DATE
        defaultSuspensionShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where endDate is less than or equal to DEFAULT_END_DATE
        defaultSuspensionShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the suspensionList where endDate is less than or equal to SMALLER_END_DATE
        defaultSuspensionShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where endDate is less than DEFAULT_END_DATE
        defaultSuspensionShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the suspensionList where endDate is less than UPDATED_END_DATE
        defaultSuspensionShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList where endDate is greater than DEFAULT_END_DATE
        defaultSuspensionShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the suspensionList where endDate is greater than SMALLER_END_DATE
        defaultSuspensionShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSuspensionsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = suspension.getUser();
        suspensionRepository.saveAndFlush(suspension);
        Long userId = user.getId();

        // Get all the suspensionList where user equals to userId
        defaultSuspensionShouldBeFound("userId.equals=" + userId);

        // Get all the suspensionList where user equals to (userId + 1)
        defaultSuspensionShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuspensionShouldBeFound(String filter) throws Exception {
        restSuspensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suspension.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restSuspensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuspensionShouldNotBeFound(String filter) throws Exception {
        restSuspensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuspensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSuspension() throws Exception {
        // Get the suspension
        restSuspensionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSuspension() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();

        // Update the suspension
        Suspension updatedSuspension = suspensionRepository.findById(suspension.getId()).get();
        // Disconnect from session so that the updates on updatedSuspension are not directly saved in db
        em.detach(updatedSuspension);
        updatedSuspension.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(updatedSuspension);

        restSuspensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, suspensionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
        Suspension testSuspension = suspensionList.get(suspensionList.size() - 1);
        assertThat(testSuspension.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSuspension.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, suspensionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(suspensionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuspensionWithPatch() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();

        // Update the suspension using partial update
        Suspension partialUpdatedSuspension = new Suspension();
        partialUpdatedSuspension.setId(suspension.getId());

        partialUpdatedSuspension.startDate(UPDATED_START_DATE);

        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuspension.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuspension))
            )
            .andExpect(status().isOk());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
        Suspension testSuspension = suspensionList.get(suspensionList.size() - 1);
        assertThat(testSuspension.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSuspension.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSuspensionWithPatch() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();

        // Update the suspension using partial update
        Suspension partialUpdatedSuspension = new Suspension();
        partialUpdatedSuspension.setId(suspension.getId());

        partialUpdatedSuspension.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuspension.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuspension))
            )
            .andExpect(status().isOk());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
        Suspension testSuspension = suspensionList.get(suspensionList.size() - 1);
        assertThat(testSuspension.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSuspension.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, suspensionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuspension() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        int databaseSizeBeforeDelete = suspensionRepository.findAll().size();

        // Delete the suspension
        restSuspensionMockMvc
            .perform(delete(ENTITY_API_URL_ID, suspension.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
