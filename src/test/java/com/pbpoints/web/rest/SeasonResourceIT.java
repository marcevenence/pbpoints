package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Season;
import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.enumeration.Status;
import com.pbpoints.repository.SeasonRepository;
import com.pbpoints.service.dto.SeasonDTO;
import com.pbpoints.service.mapper.SeasonMapper;
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
 * Integration tests for the {@link SeasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeasonResourceIT {

    private static final Integer DEFAULT_ANIO = 1;
    private static final Integer UPDATED_ANIO = 2;
    private static final Integer SMALLER_ANIO = 1 - 1;

    private static final Status DEFAULT_STATUS = Status.CANCEL;
    private static final Status UPDATED_STATUS = Status.CREATED;

    private static final String ENTITY_API_URL = "/api/seasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeasonMapper seasonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeasonMockMvc;

    private Season season;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Season createEntity(EntityManager em) {
        Season season = new Season().anio(DEFAULT_ANIO).status(DEFAULT_STATUS);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        season.setTournament(tournament);
        return season;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Season createUpdatedEntity(EntityManager em) {
        Season season = new Season().anio(UPDATED_ANIO).status(UPDATED_STATUS);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        season.setTournament(tournament);
        return season;
    }

    @BeforeEach
    public void initTest() {
        season = createEntity(em);
    }

    @Test
    @Transactional
    void createSeason() throws Exception {
        int databaseSizeBeforeCreate = seasonRepository.findAll().size();
        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);
        restSeasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seasonDTO)))
            .andExpect(status().isCreated());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeCreate + 1);
        Season testSeason = seasonList.get(seasonList.size() - 1);
        assertThat(testSeason.getAnio()).isEqualTo(DEFAULT_ANIO);
        assertThat(testSeason.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createSeasonWithExistingId() throws Exception {
        // Create the Season with an existing ID
        season.setId(1L);
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        int databaseSizeBeforeCreate = seasonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seasonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnioIsRequired() throws Exception {
        int databaseSizeBeforeTest = seasonRepository.findAll().size();
        // set the field null
        season.setAnio(null);

        // Create the Season, which fails.
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        restSeasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seasonDTO)))
            .andExpect(status().isBadRequest());

        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = seasonRepository.findAll().size();
        // set the field null
        season.setStatus(null);

        // Create the Season, which fails.
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        restSeasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seasonDTO)))
            .andExpect(status().isBadRequest());

        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeasons() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(season.getId().intValue())))
            .andExpect(jsonPath("$.[*].anio").value(hasItem(DEFAULT_ANIO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get the season
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL_ID, season.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(season.getId().intValue()))
            .andExpect(jsonPath("$.anio").value(DEFAULT_ANIO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getSeasonsByIdFiltering() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        Long id = season.getId();

        defaultSeasonShouldBeFound("id.equals=" + id);
        defaultSeasonShouldNotBeFound("id.notEquals=" + id);

        defaultSeasonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSeasonShouldNotBeFound("id.greaterThan=" + id);

        defaultSeasonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSeasonShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSeasonsByAnioIsEqualToSomething() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where anio equals to DEFAULT_ANIO
        defaultSeasonShouldBeFound("anio.equals=" + DEFAULT_ANIO);

        // Get all the seasonList where anio equals to UPDATED_ANIO
        defaultSeasonShouldNotBeFound("anio.equals=" + UPDATED_ANIO);
    }

    @Test
    @Transactional
    void getAllSeasonsByAnioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where anio not equals to DEFAULT_ANIO
        defaultSeasonShouldNotBeFound("anio.notEquals=" + DEFAULT_ANIO);

        // Get all the seasonList where anio not equals to UPDATED_ANIO
        defaultSeasonShouldBeFound("anio.notEquals=" + UPDATED_ANIO);
    }

    @Test
    @Transactional
    void getAllSeasonsByAnioIsInShouldWork() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where anio in DEFAULT_ANIO or UPDATED_ANIO
        defaultSeasonShouldBeFound("anio.in=" + DEFAULT_ANIO + "," + UPDATED_ANIO);

        // Get all the seasonList where anio equals to UPDATED_ANIO
        defaultSeasonShouldNotBeFound("anio.in=" + UPDATED_ANIO);
    }

    @Test
    @Transactional
    void getAllSeasonsByAnioIsNullOrNotNull() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where anio is not null
        defaultSeasonShouldBeFound("anio.specified=true");

        // Get all the seasonList where anio is null
        defaultSeasonShouldNotBeFound("anio.specified=false");
    }

    @Test
    @Transactional
    void getAllSeasonsByAnioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where anio is greater than or equal to DEFAULT_ANIO
        defaultSeasonShouldBeFound("anio.greaterThanOrEqual=" + DEFAULT_ANIO);

        // Get all the seasonList where anio is greater than or equal to UPDATED_ANIO
        defaultSeasonShouldNotBeFound("anio.greaterThanOrEqual=" + UPDATED_ANIO);
    }

    @Test
    @Transactional
    void getAllSeasonsByAnioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where anio is less than or equal to DEFAULT_ANIO
        defaultSeasonShouldBeFound("anio.lessThanOrEqual=" + DEFAULT_ANIO);

        // Get all the seasonList where anio is less than or equal to SMALLER_ANIO
        defaultSeasonShouldNotBeFound("anio.lessThanOrEqual=" + SMALLER_ANIO);
    }

    @Test
    @Transactional
    void getAllSeasonsByAnioIsLessThanSomething() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where anio is less than DEFAULT_ANIO
        defaultSeasonShouldNotBeFound("anio.lessThan=" + DEFAULT_ANIO);

        // Get all the seasonList where anio is less than UPDATED_ANIO
        defaultSeasonShouldBeFound("anio.lessThan=" + UPDATED_ANIO);
    }

    @Test
    @Transactional
    void getAllSeasonsByAnioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where anio is greater than DEFAULT_ANIO
        defaultSeasonShouldNotBeFound("anio.greaterThan=" + DEFAULT_ANIO);

        // Get all the seasonList where anio is greater than SMALLER_ANIO
        defaultSeasonShouldBeFound("anio.greaterThan=" + SMALLER_ANIO);
    }

    @Test
    @Transactional
    void getAllSeasonsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where status equals to DEFAULT_STATUS
        defaultSeasonShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the seasonList where status equals to UPDATED_STATUS
        defaultSeasonShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSeasonsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where status not equals to DEFAULT_STATUS
        defaultSeasonShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the seasonList where status not equals to UPDATED_STATUS
        defaultSeasonShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSeasonsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSeasonShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the seasonList where status equals to UPDATED_STATUS
        defaultSeasonShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSeasonsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasonList where status is not null
        defaultSeasonShouldBeFound("status.specified=true");

        // Get all the seasonList where status is null
        defaultSeasonShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSeasonsByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        em.persist(tournament);
        em.flush();
        season.setTournament(tournament);
        seasonRepository.saveAndFlush(season);
        Long tournamentId = tournament.getId();

        // Get all the seasonList where tournament equals to tournamentId
        defaultSeasonShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the seasonList where tournament equals to (tournamentId + 1)
        defaultSeasonShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSeasonShouldBeFound(String filter) throws Exception {
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(season.getId().intValue())))
            .andExpect(jsonPath("$.[*].anio").value(hasItem(DEFAULT_ANIO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSeasonShouldNotBeFound(String filter) throws Exception {
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSeason() throws Exception {
        // Get the season
        restSeasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();

        // Update the season
        Season updatedSeason = seasonRepository.findById(season.getId()).get();
        // Disconnect from session so that the updates on updatedSeason are not directly saved in db
        em.detach(updatedSeason);
        updatedSeason.anio(UPDATED_ANIO).status(UPDATED_STATUS);
        SeasonDTO seasonDTO = seasonMapper.toDto(updatedSeason);

        restSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seasonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seasonDTO))
            )
            .andExpect(status().isOk());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeUpdate);
        Season testSeason = seasonList.get(seasonList.size() - 1);
        assertThat(testSeason.getAnio()).isEqualTo(UPDATED_ANIO);
        assertThat(testSeason.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingSeason() throws Exception {
        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();
        season.setId(count.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seasonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeason() throws Exception {
        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();
        season.setId(count.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeason() throws Exception {
        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();
        season.setId(count.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seasonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeasonWithPatch() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();

        // Update the season using partial update
        Season partialUpdatedSeason = new Season();
        partialUpdatedSeason.setId(season.getId());

        partialUpdatedSeason.status(UPDATED_STATUS);

        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeason))
            )
            .andExpect(status().isOk());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeUpdate);
        Season testSeason = seasonList.get(seasonList.size() - 1);
        assertThat(testSeason.getAnio()).isEqualTo(DEFAULT_ANIO);
        assertThat(testSeason.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSeasonWithPatch() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();

        // Update the season using partial update
        Season partialUpdatedSeason = new Season();
        partialUpdatedSeason.setId(season.getId());

        partialUpdatedSeason.anio(UPDATED_ANIO).status(UPDATED_STATUS);

        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeason))
            )
            .andExpect(status().isOk());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeUpdate);
        Season testSeason = seasonList.get(seasonList.size() - 1);
        assertThat(testSeason.getAnio()).isEqualTo(UPDATED_ANIO);
        assertThat(testSeason.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSeason() throws Exception {
        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();
        season.setId(count.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seasonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeason() throws Exception {
        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();
        season.setId(count.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeason() throws Exception {
        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();
        season.setId(count.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seasonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Season in the database
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        int databaseSizeBeforeDelete = seasonRepository.findAll().size();

        // Delete the season
        restSeasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, season.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Season> seasonList = seasonRepository.findAll();
        assertThat(seasonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
