package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Event;
import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.User;
import com.pbpoints.domain.enumeration.Status;
import com.pbpoints.repository.TournamentRepository;
import com.pbpoints.service.criteria.TournamentCriteria;
import com.pbpoints.service.dto.TournamentDTO;
import com.pbpoints.service.mapper.TournamentMapper;
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
 * Integration tests for the {@link TournamentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TournamentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLOSE_INSCR_DAYS = 1;
    private static final Integer UPDATED_CLOSE_INSCR_DAYS = 2;
    private static final Integer SMALLER_CLOSE_INSCR_DAYS = 1 - 1;

    private static final Status DEFAULT_STATUS = Status.CREATED;
    private static final Status UPDATED_STATUS = Status.PENDING;

    private static final Boolean DEFAULT_CATEGORIZE = false;
    private static final Boolean UPDATED_CATEGORIZE = true;

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_CANT_PLAYERS_NEXT_CATEGORY = 1;
    private static final Integer UPDATED_CANT_PLAYERS_NEXT_CATEGORY = 2;
    private static final Integer SMALLER_CANT_PLAYERS_NEXT_CATEGORY = 1 - 1;

    private static final Integer DEFAULT_QTY_TEAM_GROUPS = 1;
    private static final Integer UPDATED_QTY_TEAM_GROUPS = 2;
    private static final Integer SMALLER_QTY_TEAM_GROUPS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/tournaments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentMapper tournamentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTournamentMockMvc;

    private Tournament tournament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tournament createEntity(EntityManager em) {
        Tournament tournament = new Tournament()
            .name(DEFAULT_NAME)
            .closeInscrDays(DEFAULT_CLOSE_INSCR_DAYS)
            .status(DEFAULT_STATUS)
            .categorize(DEFAULT_CATEGORIZE)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .cantPlayersNextCategory(DEFAULT_CANT_PLAYERS_NEXT_CATEGORY)
            .qtyTeamGroups(DEFAULT_QTY_TEAM_GROUPS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        tournament.setOwner(user);
        return tournament;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tournament createUpdatedEntity(EntityManager em) {
        Tournament tournament = new Tournament()
            .name(UPDATED_NAME)
            .closeInscrDays(UPDATED_CLOSE_INSCR_DAYS)
            .status(UPDATED_STATUS)
            .categorize(UPDATED_CATEGORIZE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .cantPlayersNextCategory(UPDATED_CANT_PLAYERS_NEXT_CATEGORY)
            .qtyTeamGroups(UPDATED_QTY_TEAM_GROUPS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        tournament.setOwner(user);
        return tournament;
    }

    @BeforeEach
    public void initTest() {
        tournament = createEntity(em);
    }

    @Test
    @Transactional
    void createTournament() throws Exception {
        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();
        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);
        restTournamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isCreated());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate + 1);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTournament.getCloseInscrDays()).isEqualTo(DEFAULT_CLOSE_INSCR_DAYS);
        assertThat(testTournament.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTournament.getCategorize()).isEqualTo(DEFAULT_CATEGORIZE);
        assertThat(testTournament.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testTournament.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testTournament.getCantPlayersNextCategory()).isEqualTo(DEFAULT_CANT_PLAYERS_NEXT_CATEGORY);
        assertThat(testTournament.getQtyTeamGroups()).isEqualTo(DEFAULT_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void createTournamentWithExistingId() throws Exception {
        // Create the Tournament with an existing ID
        tournament.setId(1L);
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTournamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTournaments() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournament.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].closeInscrDays").value(hasItem(DEFAULT_CLOSE_INSCR_DAYS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].categorize").value(hasItem(DEFAULT_CATEGORIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].cantPlayersNextCategory").value(hasItem(DEFAULT_CANT_PLAYERS_NEXT_CATEGORY)))
            .andExpect(jsonPath("$.[*].qtyTeamGroups").value(hasItem(DEFAULT_QTY_TEAM_GROUPS)));
    }

    @Test
    @Transactional
    void getTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get the tournament
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL_ID, tournament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tournament.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.closeInscrDays").value(DEFAULT_CLOSE_INSCR_DAYS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.categorize").value(DEFAULT_CATEGORIZE.booleanValue()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.cantPlayersNextCategory").value(DEFAULT_CANT_PLAYERS_NEXT_CATEGORY))
            .andExpect(jsonPath("$.qtyTeamGroups").value(DEFAULT_QTY_TEAM_GROUPS));
    }

    @Test
    @Transactional
    void getTournamentsByIdFiltering() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        Long id = tournament.getId();

        defaultTournamentShouldBeFound("id.equals=" + id);
        defaultTournamentShouldNotBeFound("id.notEquals=" + id);

        defaultTournamentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTournamentShouldNotBeFound("id.greaterThan=" + id);

        defaultTournamentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTournamentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name equals to DEFAULT_NAME
        defaultTournamentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tournamentList where name equals to UPDATED_NAME
        defaultTournamentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name not equals to DEFAULT_NAME
        defaultTournamentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tournamentList where name not equals to UPDATED_NAME
        defaultTournamentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTournamentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tournamentList where name equals to UPDATED_NAME
        defaultTournamentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name is not null
        defaultTournamentShouldBeFound("name.specified=true");

        // Get all the tournamentList where name is null
        defaultTournamentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTournamentsByNameContainsSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name contains DEFAULT_NAME
        defaultTournamentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tournamentList where name contains UPDATED_NAME
        defaultTournamentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name does not contain DEFAULT_NAME
        defaultTournamentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tournamentList where name does not contain UPDATED_NAME
        defaultTournamentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTournamentsByCloseInscrDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where closeInscrDays equals to DEFAULT_CLOSE_INSCR_DAYS
        defaultTournamentShouldBeFound("closeInscrDays.equals=" + DEFAULT_CLOSE_INSCR_DAYS);

        // Get all the tournamentList where closeInscrDays equals to UPDATED_CLOSE_INSCR_DAYS
        defaultTournamentShouldNotBeFound("closeInscrDays.equals=" + UPDATED_CLOSE_INSCR_DAYS);
    }

    @Test
    @Transactional
    void getAllTournamentsByCloseInscrDaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where closeInscrDays not equals to DEFAULT_CLOSE_INSCR_DAYS
        defaultTournamentShouldNotBeFound("closeInscrDays.notEquals=" + DEFAULT_CLOSE_INSCR_DAYS);

        // Get all the tournamentList where closeInscrDays not equals to UPDATED_CLOSE_INSCR_DAYS
        defaultTournamentShouldBeFound("closeInscrDays.notEquals=" + UPDATED_CLOSE_INSCR_DAYS);
    }

    @Test
    @Transactional
    void getAllTournamentsByCloseInscrDaysIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where closeInscrDays in DEFAULT_CLOSE_INSCR_DAYS or UPDATED_CLOSE_INSCR_DAYS
        defaultTournamentShouldBeFound("closeInscrDays.in=" + DEFAULT_CLOSE_INSCR_DAYS + "," + UPDATED_CLOSE_INSCR_DAYS);

        // Get all the tournamentList where closeInscrDays equals to UPDATED_CLOSE_INSCR_DAYS
        defaultTournamentShouldNotBeFound("closeInscrDays.in=" + UPDATED_CLOSE_INSCR_DAYS);
    }

    @Test
    @Transactional
    void getAllTournamentsByCloseInscrDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where closeInscrDays is not null
        defaultTournamentShouldBeFound("closeInscrDays.specified=true");

        // Get all the tournamentList where closeInscrDays is null
        defaultTournamentShouldNotBeFound("closeInscrDays.specified=false");
    }

    @Test
    @Transactional
    void getAllTournamentsByCloseInscrDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where closeInscrDays is greater than or equal to DEFAULT_CLOSE_INSCR_DAYS
        defaultTournamentShouldBeFound("closeInscrDays.greaterThanOrEqual=" + DEFAULT_CLOSE_INSCR_DAYS);

        // Get all the tournamentList where closeInscrDays is greater than or equal to UPDATED_CLOSE_INSCR_DAYS
        defaultTournamentShouldNotBeFound("closeInscrDays.greaterThanOrEqual=" + UPDATED_CLOSE_INSCR_DAYS);
    }

    @Test
    @Transactional
    void getAllTournamentsByCloseInscrDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where closeInscrDays is less than or equal to DEFAULT_CLOSE_INSCR_DAYS
        defaultTournamentShouldBeFound("closeInscrDays.lessThanOrEqual=" + DEFAULT_CLOSE_INSCR_DAYS);

        // Get all the tournamentList where closeInscrDays is less than or equal to SMALLER_CLOSE_INSCR_DAYS
        defaultTournamentShouldNotBeFound("closeInscrDays.lessThanOrEqual=" + SMALLER_CLOSE_INSCR_DAYS);
    }

    @Test
    @Transactional
    void getAllTournamentsByCloseInscrDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where closeInscrDays is less than DEFAULT_CLOSE_INSCR_DAYS
        defaultTournamentShouldNotBeFound("closeInscrDays.lessThan=" + DEFAULT_CLOSE_INSCR_DAYS);

        // Get all the tournamentList where closeInscrDays is less than UPDATED_CLOSE_INSCR_DAYS
        defaultTournamentShouldBeFound("closeInscrDays.lessThan=" + UPDATED_CLOSE_INSCR_DAYS);
    }

    @Test
    @Transactional
    void getAllTournamentsByCloseInscrDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where closeInscrDays is greater than DEFAULT_CLOSE_INSCR_DAYS
        defaultTournamentShouldNotBeFound("closeInscrDays.greaterThan=" + DEFAULT_CLOSE_INSCR_DAYS);

        // Get all the tournamentList where closeInscrDays is greater than SMALLER_CLOSE_INSCR_DAYS
        defaultTournamentShouldBeFound("closeInscrDays.greaterThan=" + SMALLER_CLOSE_INSCR_DAYS);
    }

    @Test
    @Transactional
    void getAllTournamentsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where status equals to DEFAULT_STATUS
        defaultTournamentShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the tournamentList where status equals to UPDATED_STATUS
        defaultTournamentShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTournamentsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where status not equals to DEFAULT_STATUS
        defaultTournamentShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the tournamentList where status not equals to UPDATED_STATUS
        defaultTournamentShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTournamentsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTournamentShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the tournamentList where status equals to UPDATED_STATUS
        defaultTournamentShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTournamentsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where status is not null
        defaultTournamentShouldBeFound("status.specified=true");

        // Get all the tournamentList where status is null
        defaultTournamentShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTournamentsByCategorizeIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where categorize equals to DEFAULT_CATEGORIZE
        defaultTournamentShouldBeFound("categorize.equals=" + DEFAULT_CATEGORIZE);

        // Get all the tournamentList where categorize equals to UPDATED_CATEGORIZE
        defaultTournamentShouldNotBeFound("categorize.equals=" + UPDATED_CATEGORIZE);
    }

    @Test
    @Transactional
    void getAllTournamentsByCategorizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where categorize not equals to DEFAULT_CATEGORIZE
        defaultTournamentShouldNotBeFound("categorize.notEquals=" + DEFAULT_CATEGORIZE);

        // Get all the tournamentList where categorize not equals to UPDATED_CATEGORIZE
        defaultTournamentShouldBeFound("categorize.notEquals=" + UPDATED_CATEGORIZE);
    }

    @Test
    @Transactional
    void getAllTournamentsByCategorizeIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where categorize in DEFAULT_CATEGORIZE or UPDATED_CATEGORIZE
        defaultTournamentShouldBeFound("categorize.in=" + DEFAULT_CATEGORIZE + "," + UPDATED_CATEGORIZE);

        // Get all the tournamentList where categorize equals to UPDATED_CATEGORIZE
        defaultTournamentShouldNotBeFound("categorize.in=" + UPDATED_CATEGORIZE);
    }

    @Test
    @Transactional
    void getAllTournamentsByCategorizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where categorize is not null
        defaultTournamentShouldBeFound("categorize.specified=true");

        // Get all the tournamentList where categorize is null
        defaultTournamentShouldNotBeFound("categorize.specified=false");
    }

    @Test
    @Transactional
    void getAllTournamentsByCantPlayersNextCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where cantPlayersNextCategory equals to DEFAULT_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldBeFound("cantPlayersNextCategory.equals=" + DEFAULT_CANT_PLAYERS_NEXT_CATEGORY);

        // Get all the tournamentList where cantPlayersNextCategory equals to UPDATED_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldNotBeFound("cantPlayersNextCategory.equals=" + UPDATED_CANT_PLAYERS_NEXT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllTournamentsByCantPlayersNextCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where cantPlayersNextCategory not equals to DEFAULT_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldNotBeFound("cantPlayersNextCategory.notEquals=" + DEFAULT_CANT_PLAYERS_NEXT_CATEGORY);

        // Get all the tournamentList where cantPlayersNextCategory not equals to UPDATED_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldBeFound("cantPlayersNextCategory.notEquals=" + UPDATED_CANT_PLAYERS_NEXT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllTournamentsByCantPlayersNextCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where cantPlayersNextCategory in DEFAULT_CANT_PLAYERS_NEXT_CATEGORY or UPDATED_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldBeFound(
            "cantPlayersNextCategory.in=" + DEFAULT_CANT_PLAYERS_NEXT_CATEGORY + "," + UPDATED_CANT_PLAYERS_NEXT_CATEGORY
        );

        // Get all the tournamentList where cantPlayersNextCategory equals to UPDATED_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldNotBeFound("cantPlayersNextCategory.in=" + UPDATED_CANT_PLAYERS_NEXT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllTournamentsByCantPlayersNextCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where cantPlayersNextCategory is not null
        defaultTournamentShouldBeFound("cantPlayersNextCategory.specified=true");

        // Get all the tournamentList where cantPlayersNextCategory is null
        defaultTournamentShouldNotBeFound("cantPlayersNextCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllTournamentsByCantPlayersNextCategoryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where cantPlayersNextCategory is greater than or equal to DEFAULT_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldBeFound("cantPlayersNextCategory.greaterThanOrEqual=" + DEFAULT_CANT_PLAYERS_NEXT_CATEGORY);

        // Get all the tournamentList where cantPlayersNextCategory is greater than or equal to UPDATED_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldNotBeFound("cantPlayersNextCategory.greaterThanOrEqual=" + UPDATED_CANT_PLAYERS_NEXT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllTournamentsByCantPlayersNextCategoryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where cantPlayersNextCategory is less than or equal to DEFAULT_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldBeFound("cantPlayersNextCategory.lessThanOrEqual=" + DEFAULT_CANT_PLAYERS_NEXT_CATEGORY);

        // Get all the tournamentList where cantPlayersNextCategory is less than or equal to SMALLER_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldNotBeFound("cantPlayersNextCategory.lessThanOrEqual=" + SMALLER_CANT_PLAYERS_NEXT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllTournamentsByCantPlayersNextCategoryIsLessThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where cantPlayersNextCategory is less than DEFAULT_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldNotBeFound("cantPlayersNextCategory.lessThan=" + DEFAULT_CANT_PLAYERS_NEXT_CATEGORY);

        // Get all the tournamentList where cantPlayersNextCategory is less than UPDATED_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldBeFound("cantPlayersNextCategory.lessThan=" + UPDATED_CANT_PLAYERS_NEXT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllTournamentsByCantPlayersNextCategoryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where cantPlayersNextCategory is greater than DEFAULT_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldNotBeFound("cantPlayersNextCategory.greaterThan=" + DEFAULT_CANT_PLAYERS_NEXT_CATEGORY);

        // Get all the tournamentList where cantPlayersNextCategory is greater than SMALLER_CANT_PLAYERS_NEXT_CATEGORY
        defaultTournamentShouldBeFound("cantPlayersNextCategory.greaterThan=" + SMALLER_CANT_PLAYERS_NEXT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllTournamentsByQtyTeamGroupsIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where qtyTeamGroups equals to DEFAULT_QTY_TEAM_GROUPS
        defaultTournamentShouldBeFound("qtyTeamGroups.equals=" + DEFAULT_QTY_TEAM_GROUPS);

        // Get all the tournamentList where qtyTeamGroups equals to UPDATED_QTY_TEAM_GROUPS
        defaultTournamentShouldNotBeFound("qtyTeamGroups.equals=" + UPDATED_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void getAllTournamentsByQtyTeamGroupsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where qtyTeamGroups not equals to DEFAULT_QTY_TEAM_GROUPS
        defaultTournamentShouldNotBeFound("qtyTeamGroups.notEquals=" + DEFAULT_QTY_TEAM_GROUPS);

        // Get all the tournamentList where qtyTeamGroups not equals to UPDATED_QTY_TEAM_GROUPS
        defaultTournamentShouldBeFound("qtyTeamGroups.notEquals=" + UPDATED_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void getAllTournamentsByQtyTeamGroupsIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where qtyTeamGroups in DEFAULT_QTY_TEAM_GROUPS or UPDATED_QTY_TEAM_GROUPS
        defaultTournamentShouldBeFound("qtyTeamGroups.in=" + DEFAULT_QTY_TEAM_GROUPS + "," + UPDATED_QTY_TEAM_GROUPS);

        // Get all the tournamentList where qtyTeamGroups equals to UPDATED_QTY_TEAM_GROUPS
        defaultTournamentShouldNotBeFound("qtyTeamGroups.in=" + UPDATED_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void getAllTournamentsByQtyTeamGroupsIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where qtyTeamGroups is not null
        defaultTournamentShouldBeFound("qtyTeamGroups.specified=true");

        // Get all the tournamentList where qtyTeamGroups is null
        defaultTournamentShouldNotBeFound("qtyTeamGroups.specified=false");
    }

    @Test
    @Transactional
    void getAllTournamentsByQtyTeamGroupsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where qtyTeamGroups is greater than or equal to DEFAULT_QTY_TEAM_GROUPS
        defaultTournamentShouldBeFound("qtyTeamGroups.greaterThanOrEqual=" + DEFAULT_QTY_TEAM_GROUPS);

        // Get all the tournamentList where qtyTeamGroups is greater than or equal to UPDATED_QTY_TEAM_GROUPS
        defaultTournamentShouldNotBeFound("qtyTeamGroups.greaterThanOrEqual=" + UPDATED_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void getAllTournamentsByQtyTeamGroupsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where qtyTeamGroups is less than or equal to DEFAULT_QTY_TEAM_GROUPS
        defaultTournamentShouldBeFound("qtyTeamGroups.lessThanOrEqual=" + DEFAULT_QTY_TEAM_GROUPS);

        // Get all the tournamentList where qtyTeamGroups is less than or equal to SMALLER_QTY_TEAM_GROUPS
        defaultTournamentShouldNotBeFound("qtyTeamGroups.lessThanOrEqual=" + SMALLER_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void getAllTournamentsByQtyTeamGroupsIsLessThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where qtyTeamGroups is less than DEFAULT_QTY_TEAM_GROUPS
        defaultTournamentShouldNotBeFound("qtyTeamGroups.lessThan=" + DEFAULT_QTY_TEAM_GROUPS);

        // Get all the tournamentList where qtyTeamGroups is less than UPDATED_QTY_TEAM_GROUPS
        defaultTournamentShouldBeFound("qtyTeamGroups.lessThan=" + UPDATED_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void getAllTournamentsByQtyTeamGroupsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where qtyTeamGroups is greater than DEFAULT_QTY_TEAM_GROUPS
        defaultTournamentShouldNotBeFound("qtyTeamGroups.greaterThan=" + DEFAULT_QTY_TEAM_GROUPS);

        // Get all the tournamentList where qtyTeamGroups is greater than SMALLER_QTY_TEAM_GROUPS
        defaultTournamentShouldBeFound("qtyTeamGroups.greaterThan=" + SMALLER_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void getAllTournamentsByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);
        Event event = EventResourceIT.createEntity(em);
        em.persist(event);
        em.flush();
        tournament.addEvent(event);
        tournamentRepository.saveAndFlush(tournament);
        Long eventId = event.getId();

        // Get all the tournamentList where event equals to eventId
        defaultTournamentShouldBeFound("eventId.equals=" + eventId);

        // Get all the tournamentList where event equals to (eventId + 1)
        defaultTournamentShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllTournamentsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);
        User owner = UserResourceIT.createEntity(em);
        em.persist(owner);
        em.flush();
        tournament.setOwner(owner);
        tournamentRepository.saveAndFlush(tournament);
        Long ownerId = owner.getId();

        // Get all the tournamentList where owner equals to ownerId
        defaultTournamentShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the tournamentList where owner equals to (ownerId + 1)
        defaultTournamentShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTournamentShouldBeFound(String filter) throws Exception {
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournament.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].closeInscrDays").value(hasItem(DEFAULT_CLOSE_INSCR_DAYS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].categorize").value(hasItem(DEFAULT_CATEGORIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].cantPlayersNextCategory").value(hasItem(DEFAULT_CANT_PLAYERS_NEXT_CATEGORY)))
            .andExpect(jsonPath("$.[*].qtyTeamGroups").value(hasItem(DEFAULT_QTY_TEAM_GROUPS)));

        // Check, that the count call also returns 1
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTournamentShouldNotBeFound(String filter) throws Exception {
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTournamentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTournament() throws Exception {
        // Get the tournament
        restTournamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Update the tournament
        Tournament updatedTournament = tournamentRepository.findById(tournament.getId()).get();
        // Disconnect from session so that the updates on updatedTournament are not directly saved in db
        em.detach(updatedTournament);
        updatedTournament
            .name(UPDATED_NAME)
            .closeInscrDays(UPDATED_CLOSE_INSCR_DAYS)
            .status(UPDATED_STATUS)
            .categorize(UPDATED_CATEGORIZE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .cantPlayersNextCategory(UPDATED_CANT_PLAYERS_NEXT_CATEGORY)
            .qtyTeamGroups(UPDATED_QTY_TEAM_GROUPS);
        TournamentDTO tournamentDTO = tournamentMapper.toDto(updatedTournament);

        restTournamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tournamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTournament.getCloseInscrDays()).isEqualTo(UPDATED_CLOSE_INSCR_DAYS);
        assertThat(testTournament.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTournament.getCategorize()).isEqualTo(UPDATED_CATEGORIZE);
        assertThat(testTournament.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testTournament.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testTournament.getCantPlayersNextCategory()).isEqualTo(UPDATED_CANT_PLAYERS_NEXT_CATEGORY);
        assertThat(testTournament.getQtyTeamGroups()).isEqualTo(UPDATED_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void putNonExistingTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tournamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTournamentWithPatch() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Update the tournament using partial update
        Tournament partialUpdatedTournament = new Tournament();
        partialUpdatedTournament.setId(tournament.getId());

        partialUpdatedTournament
            .closeInscrDays(UPDATED_CLOSE_INSCR_DAYS)
            .status(UPDATED_STATUS)
            .categorize(UPDATED_CATEGORIZE)
            .cantPlayersNextCategory(UPDATED_CANT_PLAYERS_NEXT_CATEGORY)
            .qtyTeamGroups(UPDATED_QTY_TEAM_GROUPS);

        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTournament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTournament))
            )
            .andExpect(status().isOk());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTournament.getCloseInscrDays()).isEqualTo(UPDATED_CLOSE_INSCR_DAYS);
        assertThat(testTournament.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTournament.getCategorize()).isEqualTo(UPDATED_CATEGORIZE);
        assertThat(testTournament.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testTournament.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testTournament.getCantPlayersNextCategory()).isEqualTo(UPDATED_CANT_PLAYERS_NEXT_CATEGORY);
        assertThat(testTournament.getQtyTeamGroups()).isEqualTo(UPDATED_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void fullUpdateTournamentWithPatch() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Update the tournament using partial update
        Tournament partialUpdatedTournament = new Tournament();
        partialUpdatedTournament.setId(tournament.getId());

        partialUpdatedTournament
            .name(UPDATED_NAME)
            .closeInscrDays(UPDATED_CLOSE_INSCR_DAYS)
            .status(UPDATED_STATUS)
            .categorize(UPDATED_CATEGORIZE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .cantPlayersNextCategory(UPDATED_CANT_PLAYERS_NEXT_CATEGORY)
            .qtyTeamGroups(UPDATED_QTY_TEAM_GROUPS);

        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTournament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTournament))
            )
            .andExpect(status().isOk());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTournament.getCloseInscrDays()).isEqualTo(UPDATED_CLOSE_INSCR_DAYS);
        assertThat(testTournament.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTournament.getCategorize()).isEqualTo(UPDATED_CATEGORIZE);
        assertThat(testTournament.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testTournament.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testTournament.getCantPlayersNextCategory()).isEqualTo(UPDATED_CANT_PLAYERS_NEXT_CATEGORY);
        assertThat(testTournament.getQtyTeamGroups()).isEqualTo(UPDATED_QTY_TEAM_GROUPS);
    }

    @Test
    @Transactional
    void patchNonExistingTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tournamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();
        tournament.setId(count.incrementAndGet());

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTournamentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tournamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeDelete = tournamentRepository.findAll().size();

        // Delete the tournament
        restTournamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, tournament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
