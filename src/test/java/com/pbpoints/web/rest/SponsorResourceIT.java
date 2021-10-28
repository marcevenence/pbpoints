package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Sponsor;
import com.pbpoints.domain.Tournament;
import com.pbpoints.repository.SponsorRepository;
import com.pbpoints.service.criteria.SponsorCriteria;
import com.pbpoints.service.dto.SponsorDTO;
import com.pbpoints.service.mapper.SponsorMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SponsorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SponsorResourceIT {

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/sponsors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private SponsorMapper sponsorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSponsorMockMvc;

    private Sponsor sponsor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sponsor createEntity(EntityManager em) {
        Sponsor sponsor = new Sponsor()
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE);
        return sponsor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sponsor createUpdatedEntity(EntityManager em) {
        Sponsor sponsor = new Sponsor()
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE);
        return sponsor;
    }

    @BeforeEach
    public void initTest() {
        sponsor = createEntity(em);
    }

    @Test
    @Transactional
    void createSponsor() throws Exception {
        int databaseSizeBeforeCreate = sponsorRepository.findAll().size();
        // Create the Sponsor
        SponsorDTO sponsorDTO = sponsorMapper.toDto(sponsor);
        restSponsorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sponsorDTO)))
            .andExpect(status().isCreated());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeCreate + 1);
        Sponsor testSponsor = sponsorList.get(sponsorList.size() - 1);
        assertThat(testSponsor.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSponsor.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testSponsor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSponsor.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createSponsorWithExistingId() throws Exception {
        // Create the Sponsor with an existing ID
        sponsor.setId(1L);
        SponsorDTO sponsorDTO = sponsorMapper.toDto(sponsor);

        int databaseSizeBeforeCreate = sponsorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSponsorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sponsorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sponsorRepository.findAll().size();
        // set the field null
        sponsor.setName(null);

        // Create the Sponsor, which fails.
        SponsorDTO sponsorDTO = sponsorMapper.toDto(sponsor);

        restSponsorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sponsorDTO)))
            .andExpect(status().isBadRequest());

        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSponsors() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList
        restSponsorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sponsor.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSponsor() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get the sponsor
        restSponsorMockMvc
            .perform(get(ENTITY_API_URL_ID, sponsor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sponsor.getId().intValue()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getSponsorsByIdFiltering() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        Long id = sponsor.getId();

        defaultSponsorShouldBeFound("id.equals=" + id);
        defaultSponsorShouldNotBeFound("id.notEquals=" + id);

        defaultSponsorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSponsorShouldNotBeFound("id.greaterThan=" + id);

        defaultSponsorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSponsorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSponsorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where name equals to DEFAULT_NAME
        defaultSponsorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sponsorList where name equals to UPDATED_NAME
        defaultSponsorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSponsorsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where name not equals to DEFAULT_NAME
        defaultSponsorShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the sponsorList where name not equals to UPDATED_NAME
        defaultSponsorShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSponsorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSponsorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sponsorList where name equals to UPDATED_NAME
        defaultSponsorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSponsorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where name is not null
        defaultSponsorShouldBeFound("name.specified=true");

        // Get all the sponsorList where name is null
        defaultSponsorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSponsorsByNameContainsSomething() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where name contains DEFAULT_NAME
        defaultSponsorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sponsorList where name contains UPDATED_NAME
        defaultSponsorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSponsorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where name does not contain DEFAULT_NAME
        defaultSponsorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sponsorList where name does not contain UPDATED_NAME
        defaultSponsorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSponsorsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where active equals to DEFAULT_ACTIVE
        defaultSponsorShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the sponsorList where active equals to UPDATED_ACTIVE
        defaultSponsorShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSponsorsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where active not equals to DEFAULT_ACTIVE
        defaultSponsorShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the sponsorList where active not equals to UPDATED_ACTIVE
        defaultSponsorShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSponsorsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultSponsorShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the sponsorList where active equals to UPDATED_ACTIVE
        defaultSponsorShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSponsorsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList where active is not null
        defaultSponsorShouldBeFound("active.specified=true");

        // Get all the sponsorList where active is null
        defaultSponsorShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllSponsorsByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        sponsor.setTournament(tournament);
        sponsorRepository.saveAndFlush(sponsor);
        Long tournamentId = tournament.getId();

        // Get all the sponsorList where tournament equals to tournamentId
        defaultSponsorShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the sponsorList where tournament equals to (tournamentId + 1)
        defaultSponsorShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSponsorShouldBeFound(String filter) throws Exception {
        restSponsorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sponsor.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restSponsorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSponsorShouldNotBeFound(String filter) throws Exception {
        restSponsorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSponsorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSponsor() throws Exception {
        // Get the sponsor
        restSponsorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSponsor() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();

        // Update the sponsor
        Sponsor updatedSponsor = sponsorRepository.findById(sponsor.getId()).get();
        // Disconnect from session so that the updates on updatedSponsor are not directly saved in db
        em.detach(updatedSponsor);
        updatedSponsor.logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE).name(UPDATED_NAME).active(UPDATED_ACTIVE);
        SponsorDTO sponsorDTO = sponsorMapper.toDto(updatedSponsor);

        restSponsorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sponsorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sponsorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
        Sponsor testSponsor = sponsorList.get(sponsorList.size() - 1);
        assertThat(testSponsor.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSponsor.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testSponsor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSponsor.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSponsor() throws Exception {
        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();
        sponsor.setId(count.incrementAndGet());

        // Create the Sponsor
        SponsorDTO sponsorDTO = sponsorMapper.toDto(sponsor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSponsorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sponsorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sponsorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSponsor() throws Exception {
        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();
        sponsor.setId(count.incrementAndGet());

        // Create the Sponsor
        SponsorDTO sponsorDTO = sponsorMapper.toDto(sponsor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSponsorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sponsorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSponsor() throws Exception {
        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();
        sponsor.setId(count.incrementAndGet());

        // Create the Sponsor
        SponsorDTO sponsorDTO = sponsorMapper.toDto(sponsor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSponsorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sponsorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSponsorWithPatch() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();

        // Update the sponsor using partial update
        Sponsor partialUpdatedSponsor = new Sponsor();
        partialUpdatedSponsor.setId(sponsor.getId());

        partialUpdatedSponsor.name(UPDATED_NAME);

        restSponsorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSponsor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSponsor))
            )
            .andExpect(status().isOk());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
        Sponsor testSponsor = sponsorList.get(sponsorList.size() - 1);
        assertThat(testSponsor.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSponsor.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testSponsor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSponsor.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSponsorWithPatch() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();

        // Update the sponsor using partial update
        Sponsor partialUpdatedSponsor = new Sponsor();
        partialUpdatedSponsor.setId(sponsor.getId());

        partialUpdatedSponsor.logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE).name(UPDATED_NAME).active(UPDATED_ACTIVE);

        restSponsorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSponsor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSponsor))
            )
            .andExpect(status().isOk());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
        Sponsor testSponsor = sponsorList.get(sponsorList.size() - 1);
        assertThat(testSponsor.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSponsor.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testSponsor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSponsor.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSponsor() throws Exception {
        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();
        sponsor.setId(count.incrementAndGet());

        // Create the Sponsor
        SponsorDTO sponsorDTO = sponsorMapper.toDto(sponsor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSponsorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sponsorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sponsorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSponsor() throws Exception {
        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();
        sponsor.setId(count.incrementAndGet());

        // Create the Sponsor
        SponsorDTO sponsorDTO = sponsorMapper.toDto(sponsor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSponsorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sponsorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSponsor() throws Exception {
        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();
        sponsor.setId(count.incrementAndGet());

        // Create the Sponsor
        SponsorDTO sponsorDTO = sponsorMapper.toDto(sponsor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSponsorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sponsorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSponsor() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        int databaseSizeBeforeDelete = sponsorRepository.findAll().size();

        // Delete the sponsor
        restSponsorMockMvc
            .perform(delete(ENTITY_API_URL_ID, sponsor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
