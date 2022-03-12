package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Format;
import com.pbpoints.domain.Tournament;
import com.pbpoints.repository.FormatRepository;
import com.pbpoints.service.criteria.FormatCriteria;
import com.pbpoints.service.dto.FormatDTO;
import com.pbpoints.service.mapper.FormatMapper;
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
 * Integration tests for the {@link FormatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormatResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PLAYERS_QTY = 1;
    private static final Integer UPDATED_PLAYERS_QTY = 2;
    private static final Integer SMALLER_PLAYERS_QTY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/formats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormatRepository formatRepository;

    @Autowired
    private FormatMapper formatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormatMockMvc;

    private Format format;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Format createEntity(EntityManager em) {
        Format format = new Format().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).playersQty(DEFAULT_PLAYERS_QTY);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        format.setTournament(tournament);
        return format;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Format createUpdatedEntity(EntityManager em) {
        Format format = new Format().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).playersQty(UPDATED_PLAYERS_QTY);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        format.setTournament(tournament);
        return format;
    }

    @BeforeEach
    public void initTest() {
        format = createEntity(em);
    }

    @Test
    @Transactional
    void createFormat() throws Exception {
        int databaseSizeBeforeCreate = formatRepository.findAll().size();
        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);
        restFormatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isCreated());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeCreate + 1);
        Format testFormat = formatList.get(formatList.size() - 1);
        assertThat(testFormat.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFormat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormat.getPlayersQty()).isEqualTo(DEFAULT_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void createFormatWithExistingId() throws Exception {
        // Create the Format with an existing ID
        format.setId(1L);
        FormatDTO formatDTO = formatMapper.toDto(format);

        int databaseSizeBeforeCreate = formatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = formatRepository.findAll().size();
        // set the field null
        format.setName(null);

        // Create the Format, which fails.
        FormatDTO formatDTO = formatMapper.toDto(format);

        restFormatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isBadRequest());

        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormats() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList
        restFormatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(format.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].playersQty").value(hasItem(DEFAULT_PLAYERS_QTY)));
    }

    @Test
    @Transactional
    void getFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get the format
        restFormatMockMvc
            .perform(get(ENTITY_API_URL_ID, format.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(format.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.playersQty").value(DEFAULT_PLAYERS_QTY));
    }

    @Test
    @Transactional
    void getFormatsByIdFiltering() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        Long id = format.getId();

        defaultFormatShouldBeFound("id.equals=" + id);
        defaultFormatShouldNotBeFound("id.notEquals=" + id);

        defaultFormatShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormatShouldNotBeFound("id.greaterThan=" + id);

        defaultFormatShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormatShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormatsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name equals to DEFAULT_NAME
        defaultFormatShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the formatList where name equals to UPDATED_NAME
        defaultFormatShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormatsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name not equals to DEFAULT_NAME
        defaultFormatShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the formatList where name not equals to UPDATED_NAME
        defaultFormatShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormatsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFormatShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the formatList where name equals to UPDATED_NAME
        defaultFormatShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormatsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name is not null
        defaultFormatShouldBeFound("name.specified=true");

        // Get all the formatList where name is null
        defaultFormatShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFormatsByNameContainsSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name contains DEFAULT_NAME
        defaultFormatShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the formatList where name contains UPDATED_NAME
        defaultFormatShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormatsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name does not contain DEFAULT_NAME
        defaultFormatShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the formatList where name does not contain UPDATED_NAME
        defaultFormatShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFormatsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description equals to DEFAULT_DESCRIPTION
        defaultFormatShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the formatList where description equals to UPDATED_DESCRIPTION
        defaultFormatShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormatsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description not equals to DEFAULT_DESCRIPTION
        defaultFormatShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the formatList where description not equals to UPDATED_DESCRIPTION
        defaultFormatShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormatsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFormatShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the formatList where description equals to UPDATED_DESCRIPTION
        defaultFormatShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormatsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description is not null
        defaultFormatShouldBeFound("description.specified=true");

        // Get all the formatList where description is null
        defaultFormatShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFormatsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description contains DEFAULT_DESCRIPTION
        defaultFormatShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the formatList where description contains UPDATED_DESCRIPTION
        defaultFormatShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormatsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description does not contain DEFAULT_DESCRIPTION
        defaultFormatShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the formatList where description does not contain UPDATED_DESCRIPTION
        defaultFormatShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormatsByPlayersQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty equals to DEFAULT_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.equals=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty equals to UPDATED_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.equals=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void getAllFormatsByPlayersQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty not equals to DEFAULT_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.notEquals=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty not equals to UPDATED_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.notEquals=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void getAllFormatsByPlayersQtyIsInShouldWork() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty in DEFAULT_PLAYERS_QTY or UPDATED_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.in=" + DEFAULT_PLAYERS_QTY + "," + UPDATED_PLAYERS_QTY);

        // Get all the formatList where playersQty equals to UPDATED_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.in=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void getAllFormatsByPlayersQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is not null
        defaultFormatShouldBeFound("playersQty.specified=true");

        // Get all the formatList where playersQty is null
        defaultFormatShouldNotBeFound("playersQty.specified=false");
    }

    @Test
    @Transactional
    void getAllFormatsByPlayersQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is greater than or equal to DEFAULT_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.greaterThanOrEqual=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty is greater than or equal to UPDATED_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.greaterThanOrEqual=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void getAllFormatsByPlayersQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is less than or equal to DEFAULT_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.lessThanOrEqual=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty is less than or equal to SMALLER_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.lessThanOrEqual=" + SMALLER_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void getAllFormatsByPlayersQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is less than DEFAULT_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.lessThan=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty is less than UPDATED_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.lessThan=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void getAllFormatsByPlayersQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is greater than DEFAULT_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.greaterThan=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty is greater than SMALLER_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.greaterThan=" + SMALLER_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void getAllFormatsByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        format.setTournament(tournament);
        formatRepository.saveAndFlush(format);
        Long tournamentId = tournament.getId();

        // Get all the formatList where tournament equals to tournamentId
        defaultFormatShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the formatList where tournament equals to (tournamentId + 1)
        defaultFormatShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormatShouldBeFound(String filter) throws Exception {
        restFormatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(format.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].playersQty").value(hasItem(DEFAULT_PLAYERS_QTY)));

        // Check, that the count call also returns 1
        restFormatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormatShouldNotBeFound(String filter) throws Exception {
        restFormatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFormat() throws Exception {
        // Get the format
        restFormatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        int databaseSizeBeforeUpdate = formatRepository.findAll().size();

        // Update the format
        Format updatedFormat = formatRepository.findById(format.getId()).get();
        // Disconnect from session so that the updates on updatedFormat are not directly saved in db
        em.detach(updatedFormat);
        updatedFormat.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).playersQty(UPDATED_PLAYERS_QTY);
        FormatDTO formatDTO = formatMapper.toDto(updatedFormat);

        restFormatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
        Format testFormat = formatList.get(formatList.size() - 1);
        assertThat(testFormat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFormat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormat.getPlayersQty()).isEqualTo(UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void putNonExistingFormat() throws Exception {
        int databaseSizeBeforeUpdate = formatRepository.findAll().size();
        format.setId(count.incrementAndGet());

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormat() throws Exception {
        int databaseSizeBeforeUpdate = formatRepository.findAll().size();
        format.setId(count.incrementAndGet());

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormat() throws Exception {
        int databaseSizeBeforeUpdate = formatRepository.findAll().size();
        format.setId(count.incrementAndGet());

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormatWithPatch() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        int databaseSizeBeforeUpdate = formatRepository.findAll().size();

        // Update the format using partial update
        Format partialUpdatedFormat = new Format();
        partialUpdatedFormat.setId(format.getId());

        partialUpdatedFormat.name(UPDATED_NAME).playersQty(UPDATED_PLAYERS_QTY);

        restFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormat))
            )
            .andExpect(status().isOk());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
        Format testFormat = formatList.get(formatList.size() - 1);
        assertThat(testFormat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFormat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormat.getPlayersQty()).isEqualTo(UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void fullUpdateFormatWithPatch() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        int databaseSizeBeforeUpdate = formatRepository.findAll().size();

        // Update the format using partial update
        Format partialUpdatedFormat = new Format();
        partialUpdatedFormat.setId(format.getId());

        partialUpdatedFormat.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).playersQty(UPDATED_PLAYERS_QTY);

        restFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormat))
            )
            .andExpect(status().isOk());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
        Format testFormat = formatList.get(formatList.size() - 1);
        assertThat(testFormat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFormat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormat.getPlayersQty()).isEqualTo(UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    void patchNonExistingFormat() throws Exception {
        int databaseSizeBeforeUpdate = formatRepository.findAll().size();
        format.setId(count.incrementAndGet());

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormat() throws Exception {
        int databaseSizeBeforeUpdate = formatRepository.findAll().size();
        format.setId(count.incrementAndGet());

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormat() throws Exception {
        int databaseSizeBeforeUpdate = formatRepository.findAll().size();
        format.setId(count.incrementAndGet());

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        int databaseSizeBeforeDelete = formatRepository.findAll().size();

        // Delete the format
        restFormatMockMvc
            .perform(delete(ENTITY_API_URL_ID, format.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
