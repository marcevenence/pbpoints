package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Game;
import com.pbpoints.domain.Team;
import com.pbpoints.domain.enumeration.Status;
import com.pbpoints.repository.GameRepository;
import com.pbpoints.service.criteria.GameCriteria;
import com.pbpoints.service.dto.GameDTO;
import com.pbpoints.service.mapper.GameMapper;
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
 * Integration tests for the {@link GameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameResourceIT {

    private static final Integer DEFAULT_POINTS_A = 1;
    private static final Integer UPDATED_POINTS_A = 2;
    private static final Integer SMALLER_POINTS_A = 1 - 1;

    private static final Integer DEFAULT_POINTS_B = 1;
    private static final Integer UPDATED_POINTS_B = 2;
    private static final Integer SMALLER_POINTS_B = 1 - 1;

    private static final Integer DEFAULT_SPLIT_DECK_NUM = 1;
    private static final Integer UPDATED_SPLIT_DECK_NUM = 2;
    private static final Integer SMALLER_SPLIT_DECK_NUM = 1 - 1;

    private static final Integer DEFAULT_TIME_LEFT = 1;
    private static final Integer UPDATED_TIME_LEFT = 2;
    private static final Integer SMALLER_TIME_LEFT = 1 - 1;

    private static final Status DEFAULT_STATUS = Status.CREATED;
    private static final Status UPDATED_STATUS = Status.PENDING;

    private static final Integer DEFAULT_OVERTIME_A = 1;
    private static final Integer UPDATED_OVERTIME_A = 2;
    private static final Integer SMALLER_OVERTIME_A = 1 - 1;

    private static final Integer DEFAULT_OVERTIME_B = 1;
    private static final Integer UPDATED_OVERTIME_B = 2;
    private static final Integer SMALLER_OVERTIME_B = 1 - 1;

    private static final Integer DEFAULT_UVU_A = 1;
    private static final Integer UPDATED_UVU_A = 2;
    private static final Integer SMALLER_UVU_A = 1 - 1;

    private static final Integer DEFAULT_UVU_B = 1;
    private static final Integer UPDATED_UVU_B = 2;
    private static final Integer SMALLER_UVU_B = 1 - 1;

    private static final Integer DEFAULT_GROUP = 1;
    private static final Integer UPDATED_GROUP = 2;
    private static final Integer SMALLER_GROUP = 1 - 1;

    private static final String DEFAULT_CLASIF = "AAAAAAAAAA";
    private static final String UPDATED_CLASIF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/games";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameMockMvc;

    private Game game;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createEntity(EntityManager em) {
        Game game = new Game()
            .pointsA(DEFAULT_POINTS_A)
            .pointsB(DEFAULT_POINTS_B)
            .splitDeckNum(DEFAULT_SPLIT_DECK_NUM)
            .timeLeft(DEFAULT_TIME_LEFT)
            .status(DEFAULT_STATUS)
            .overtimeA(DEFAULT_OVERTIME_A)
            .overtimeB(DEFAULT_OVERTIME_B)
            .uvuA(DEFAULT_UVU_A)
            .uvuB(DEFAULT_UVU_B)
            .group(DEFAULT_GROUP)
            .clasif(DEFAULT_CLASIF);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        game.setTeamA(team);
        // Add required entity
        game.setTeamB(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        game.setEventCategory(eventCategory);
        return game;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createUpdatedEntity(EntityManager em) {
        Game game = new Game()
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .splitDeckNum(UPDATED_SPLIT_DECK_NUM)
            .timeLeft(UPDATED_TIME_LEFT)
            .status(UPDATED_STATUS)
            .overtimeA(UPDATED_OVERTIME_A)
            .overtimeB(UPDATED_OVERTIME_B)
            .uvuA(UPDATED_UVU_A)
            .uvuB(UPDATED_UVU_B)
            .group(UPDATED_GROUP)
            .clasif(UPDATED_CLASIF);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        game.setTeamA(team);
        // Add required entity
        game.setTeamB(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createUpdatedEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        game.setEventCategory(eventCategory);
        return game;
    }

    @BeforeEach
    public void initTest() {
        game = createEntity(em);
    }

    @Test
    @Transactional
    void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();
        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);
        restGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getPointsA()).isEqualTo(DEFAULT_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(DEFAULT_POINTS_B);
        assertThat(testGame.getSplitDeckNum()).isEqualTo(DEFAULT_SPLIT_DECK_NUM);
        assertThat(testGame.getTimeLeft()).isEqualTo(DEFAULT_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testGame.getOvertimeA()).isEqualTo(DEFAULT_OVERTIME_A);
        assertThat(testGame.getOvertimeB()).isEqualTo(DEFAULT_OVERTIME_B);
        assertThat(testGame.getUvuA()).isEqualTo(DEFAULT_UVU_A);
        assertThat(testGame.getUvuB()).isEqualTo(DEFAULT_UVU_B);
        assertThat(testGame.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testGame.getClasif()).isEqualTo(DEFAULT_CLASIF);
    }

    @Test
    @Transactional
    void createGameWithExistingId() throws Exception {
        // Create the Game with an existing ID
        game.setId(1L);
        GameDTO gameDTO = gameMapper.toDto(game);

        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setStatus(null);

        // Create the Game, which fails.
        GameDTO gameDTO = gameMapper.toDto(game);

        restGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGames() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].pointsA").value(hasItem(DEFAULT_POINTS_A)))
            .andExpect(jsonPath("$.[*].pointsB").value(hasItem(DEFAULT_POINTS_B)))
            .andExpect(jsonPath("$.[*].splitDeckNum").value(hasItem(DEFAULT_SPLIT_DECK_NUM)))
            .andExpect(jsonPath("$.[*].timeLeft").value(hasItem(DEFAULT_TIME_LEFT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].overtimeA").value(hasItem(DEFAULT_OVERTIME_A)))
            .andExpect(jsonPath("$.[*].overtimeB").value(hasItem(DEFAULT_OVERTIME_B)))
            .andExpect(jsonPath("$.[*].uvuA").value(hasItem(DEFAULT_UVU_A)))
            .andExpect(jsonPath("$.[*].uvuB").value(hasItem(DEFAULT_UVU_B)))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP)))
            .andExpect(jsonPath("$.[*].clasif").value(hasItem(DEFAULT_CLASIF)));
    }

    @Test
    @Transactional
    void getGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc
            .perform(get(ENTITY_API_URL_ID, game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(game.getId().intValue()))
            .andExpect(jsonPath("$.pointsA").value(DEFAULT_POINTS_A))
            .andExpect(jsonPath("$.pointsB").value(DEFAULT_POINTS_B))
            .andExpect(jsonPath("$.splitDeckNum").value(DEFAULT_SPLIT_DECK_NUM))
            .andExpect(jsonPath("$.timeLeft").value(DEFAULT_TIME_LEFT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.overtimeA").value(DEFAULT_OVERTIME_A))
            .andExpect(jsonPath("$.overtimeB").value(DEFAULT_OVERTIME_B))
            .andExpect(jsonPath("$.uvuA").value(DEFAULT_UVU_A))
            .andExpect(jsonPath("$.uvuB").value(DEFAULT_UVU_B))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP))
            .andExpect(jsonPath("$.clasif").value(DEFAULT_CLASIF));
    }

    @Test
    @Transactional
    void getGamesByIdFiltering() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        Long id = game.getId();

        defaultGameShouldBeFound("id.equals=" + id);
        defaultGameShouldNotBeFound("id.notEquals=" + id);

        defaultGameShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGameShouldNotBeFound("id.greaterThan=" + id);

        defaultGameShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGameShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA equals to DEFAULT_POINTS_A
        defaultGameShouldBeFound("pointsA.equals=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA equals to UPDATED_POINTS_A
        defaultGameShouldNotBeFound("pointsA.equals=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA not equals to DEFAULT_POINTS_A
        defaultGameShouldNotBeFound("pointsA.notEquals=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA not equals to UPDATED_POINTS_A
        defaultGameShouldBeFound("pointsA.notEquals=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA in DEFAULT_POINTS_A or UPDATED_POINTS_A
        defaultGameShouldBeFound("pointsA.in=" + DEFAULT_POINTS_A + "," + UPDATED_POINTS_A);

        // Get all the gameList where pointsA equals to UPDATED_POINTS_A
        defaultGameShouldNotBeFound("pointsA.in=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is not null
        defaultGameShouldBeFound("pointsA.specified=true");

        // Get all the gameList where pointsA is null
        defaultGameShouldNotBeFound("pointsA.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is greater than or equal to DEFAULT_POINTS_A
        defaultGameShouldBeFound("pointsA.greaterThanOrEqual=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is greater than or equal to UPDATED_POINTS_A
        defaultGameShouldNotBeFound("pointsA.greaterThanOrEqual=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is less than or equal to DEFAULT_POINTS_A
        defaultGameShouldBeFound("pointsA.lessThanOrEqual=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is less than or equal to SMALLER_POINTS_A
        defaultGameShouldNotBeFound("pointsA.lessThanOrEqual=" + SMALLER_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is less than DEFAULT_POINTS_A
        defaultGameShouldNotBeFound("pointsA.lessThan=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is less than UPDATED_POINTS_A
        defaultGameShouldBeFound("pointsA.lessThan=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is greater than DEFAULT_POINTS_A
        defaultGameShouldNotBeFound("pointsA.greaterThan=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is greater than SMALLER_POINTS_A
        defaultGameShouldBeFound("pointsA.greaterThan=" + SMALLER_POINTS_A);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB equals to DEFAULT_POINTS_B
        defaultGameShouldBeFound("pointsB.equals=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB equals to UPDATED_POINTS_B
        defaultGameShouldNotBeFound("pointsB.equals=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB not equals to DEFAULT_POINTS_B
        defaultGameShouldNotBeFound("pointsB.notEquals=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB not equals to UPDATED_POINTS_B
        defaultGameShouldBeFound("pointsB.notEquals=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB in DEFAULT_POINTS_B or UPDATED_POINTS_B
        defaultGameShouldBeFound("pointsB.in=" + DEFAULT_POINTS_B + "," + UPDATED_POINTS_B);

        // Get all the gameList where pointsB equals to UPDATED_POINTS_B
        defaultGameShouldNotBeFound("pointsB.in=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is not null
        defaultGameShouldBeFound("pointsB.specified=true");

        // Get all the gameList where pointsB is null
        defaultGameShouldNotBeFound("pointsB.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is greater than or equal to DEFAULT_POINTS_B
        defaultGameShouldBeFound("pointsB.greaterThanOrEqual=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is greater than or equal to UPDATED_POINTS_B
        defaultGameShouldNotBeFound("pointsB.greaterThanOrEqual=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is less than or equal to DEFAULT_POINTS_B
        defaultGameShouldBeFound("pointsB.lessThanOrEqual=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is less than or equal to SMALLER_POINTS_B
        defaultGameShouldNotBeFound("pointsB.lessThanOrEqual=" + SMALLER_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is less than DEFAULT_POINTS_B
        defaultGameShouldNotBeFound("pointsB.lessThan=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is less than UPDATED_POINTS_B
        defaultGameShouldBeFound("pointsB.lessThan=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesByPointsBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is greater than DEFAULT_POINTS_B
        defaultGameShouldNotBeFound("pointsB.greaterThan=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is greater than SMALLER_POINTS_B
        defaultGameShouldBeFound("pointsB.greaterThan=" + SMALLER_POINTS_B);
    }

    @Test
    @Transactional
    void getAllGamesBySplitDeckNumIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum equals to DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.equals=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum equals to UPDATED_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.equals=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitDeckNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum not equals to DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.notEquals=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum not equals to UPDATED_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.notEquals=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitDeckNumIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum in DEFAULT_SPLIT_DECK_NUM or UPDATED_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.in=" + DEFAULT_SPLIT_DECK_NUM + "," + UPDATED_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum equals to UPDATED_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.in=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitDeckNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is not null
        defaultGameShouldBeFound("splitDeckNum.specified=true");

        // Get all the gameList where splitDeckNum is null
        defaultGameShouldNotBeFound("splitDeckNum.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesBySplitDeckNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is greater than or equal to DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.greaterThanOrEqual=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum is greater than or equal to UPDATED_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.greaterThanOrEqual=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitDeckNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is less than or equal to DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.lessThanOrEqual=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum is less than or equal to SMALLER_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.lessThanOrEqual=" + SMALLER_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitDeckNumIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is less than DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.lessThan=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum is less than UPDATED_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.lessThan=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesBySplitDeckNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is greater than DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.greaterThan=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum is greater than SMALLER_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.greaterThan=" + SMALLER_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft equals to DEFAULT_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.equals=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft equals to UPDATED_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.equals=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft not equals to DEFAULT_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.notEquals=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft not equals to UPDATED_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.notEquals=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft in DEFAULT_TIME_LEFT or UPDATED_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.in=" + DEFAULT_TIME_LEFT + "," + UPDATED_TIME_LEFT);

        // Get all the gameList where timeLeft equals to UPDATED_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.in=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is not null
        defaultGameShouldBeFound("timeLeft.specified=true");

        // Get all the gameList where timeLeft is null
        defaultGameShouldNotBeFound("timeLeft.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is greater than or equal to DEFAULT_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.greaterThanOrEqual=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is greater than or equal to UPDATED_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.greaterThanOrEqual=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is less than or equal to DEFAULT_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.lessThanOrEqual=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is less than or equal to SMALLER_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.lessThanOrEqual=" + SMALLER_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is less than DEFAULT_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.lessThan=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is less than UPDATED_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.lessThan=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByTimeLeftIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is greater than DEFAULT_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.greaterThan=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is greater than SMALLER_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.greaterThan=" + SMALLER_TIME_LEFT);
    }

    @Test
    @Transactional
    void getAllGamesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status equals to DEFAULT_STATUS
        defaultGameShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the gameList where status equals to UPDATED_STATUS
        defaultGameShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGamesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status not equals to DEFAULT_STATUS
        defaultGameShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the gameList where status not equals to UPDATED_STATUS
        defaultGameShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGamesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultGameShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the gameList where status equals to UPDATED_STATUS
        defaultGameShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGamesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status is not null
        defaultGameShouldBeFound("status.specified=true");

        // Get all the gameList where status is null
        defaultGameShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeAIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeA equals to DEFAULT_OVERTIME_A
        defaultGameShouldBeFound("overtimeA.equals=" + DEFAULT_OVERTIME_A);

        // Get all the gameList where overtimeA equals to UPDATED_OVERTIME_A
        defaultGameShouldNotBeFound("overtimeA.equals=" + UPDATED_OVERTIME_A);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeA not equals to DEFAULT_OVERTIME_A
        defaultGameShouldNotBeFound("overtimeA.notEquals=" + DEFAULT_OVERTIME_A);

        // Get all the gameList where overtimeA not equals to UPDATED_OVERTIME_A
        defaultGameShouldBeFound("overtimeA.notEquals=" + UPDATED_OVERTIME_A);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeAIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeA in DEFAULT_OVERTIME_A or UPDATED_OVERTIME_A
        defaultGameShouldBeFound("overtimeA.in=" + DEFAULT_OVERTIME_A + "," + UPDATED_OVERTIME_A);

        // Get all the gameList where overtimeA equals to UPDATED_OVERTIME_A
        defaultGameShouldNotBeFound("overtimeA.in=" + UPDATED_OVERTIME_A);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeAIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeA is not null
        defaultGameShouldBeFound("overtimeA.specified=true");

        // Get all the gameList where overtimeA is null
        defaultGameShouldNotBeFound("overtimeA.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeA is greater than or equal to DEFAULT_OVERTIME_A
        defaultGameShouldBeFound("overtimeA.greaterThanOrEqual=" + DEFAULT_OVERTIME_A);

        // Get all the gameList where overtimeA is greater than or equal to UPDATED_OVERTIME_A
        defaultGameShouldNotBeFound("overtimeA.greaterThanOrEqual=" + UPDATED_OVERTIME_A);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeA is less than or equal to DEFAULT_OVERTIME_A
        defaultGameShouldBeFound("overtimeA.lessThanOrEqual=" + DEFAULT_OVERTIME_A);

        // Get all the gameList where overtimeA is less than or equal to SMALLER_OVERTIME_A
        defaultGameShouldNotBeFound("overtimeA.lessThanOrEqual=" + SMALLER_OVERTIME_A);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeAIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeA is less than DEFAULT_OVERTIME_A
        defaultGameShouldNotBeFound("overtimeA.lessThan=" + DEFAULT_OVERTIME_A);

        // Get all the gameList where overtimeA is less than UPDATED_OVERTIME_A
        defaultGameShouldBeFound("overtimeA.lessThan=" + UPDATED_OVERTIME_A);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeA is greater than DEFAULT_OVERTIME_A
        defaultGameShouldNotBeFound("overtimeA.greaterThan=" + DEFAULT_OVERTIME_A);

        // Get all the gameList where overtimeA is greater than SMALLER_OVERTIME_A
        defaultGameShouldBeFound("overtimeA.greaterThan=" + SMALLER_OVERTIME_A);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeBIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeB equals to DEFAULT_OVERTIME_B
        defaultGameShouldBeFound("overtimeB.equals=" + DEFAULT_OVERTIME_B);

        // Get all the gameList where overtimeB equals to UPDATED_OVERTIME_B
        defaultGameShouldNotBeFound("overtimeB.equals=" + UPDATED_OVERTIME_B);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeB not equals to DEFAULT_OVERTIME_B
        defaultGameShouldNotBeFound("overtimeB.notEquals=" + DEFAULT_OVERTIME_B);

        // Get all the gameList where overtimeB not equals to UPDATED_OVERTIME_B
        defaultGameShouldBeFound("overtimeB.notEquals=" + UPDATED_OVERTIME_B);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeBIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeB in DEFAULT_OVERTIME_B or UPDATED_OVERTIME_B
        defaultGameShouldBeFound("overtimeB.in=" + DEFAULT_OVERTIME_B + "," + UPDATED_OVERTIME_B);

        // Get all the gameList where overtimeB equals to UPDATED_OVERTIME_B
        defaultGameShouldNotBeFound("overtimeB.in=" + UPDATED_OVERTIME_B);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeBIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeB is not null
        defaultGameShouldBeFound("overtimeB.specified=true");

        // Get all the gameList where overtimeB is null
        defaultGameShouldNotBeFound("overtimeB.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeB is greater than or equal to DEFAULT_OVERTIME_B
        defaultGameShouldBeFound("overtimeB.greaterThanOrEqual=" + DEFAULT_OVERTIME_B);

        // Get all the gameList where overtimeB is greater than or equal to UPDATED_OVERTIME_B
        defaultGameShouldNotBeFound("overtimeB.greaterThanOrEqual=" + UPDATED_OVERTIME_B);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeB is less than or equal to DEFAULT_OVERTIME_B
        defaultGameShouldBeFound("overtimeB.lessThanOrEqual=" + DEFAULT_OVERTIME_B);

        // Get all the gameList where overtimeB is less than or equal to SMALLER_OVERTIME_B
        defaultGameShouldNotBeFound("overtimeB.lessThanOrEqual=" + SMALLER_OVERTIME_B);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeBIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeB is less than DEFAULT_OVERTIME_B
        defaultGameShouldNotBeFound("overtimeB.lessThan=" + DEFAULT_OVERTIME_B);

        // Get all the gameList where overtimeB is less than UPDATED_OVERTIME_B
        defaultGameShouldBeFound("overtimeB.lessThan=" + UPDATED_OVERTIME_B);
    }

    @Test
    @Transactional
    void getAllGamesByOvertimeBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where overtimeB is greater than DEFAULT_OVERTIME_B
        defaultGameShouldNotBeFound("overtimeB.greaterThan=" + DEFAULT_OVERTIME_B);

        // Get all the gameList where overtimeB is greater than SMALLER_OVERTIME_B
        defaultGameShouldBeFound("overtimeB.greaterThan=" + SMALLER_OVERTIME_B);
    }

    @Test
    @Transactional
    void getAllGamesByUvuAIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuA equals to DEFAULT_UVU_A
        defaultGameShouldBeFound("uvuA.equals=" + DEFAULT_UVU_A);

        // Get all the gameList where uvuA equals to UPDATED_UVU_A
        defaultGameShouldNotBeFound("uvuA.equals=" + UPDATED_UVU_A);
    }

    @Test
    @Transactional
    void getAllGamesByUvuAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuA not equals to DEFAULT_UVU_A
        defaultGameShouldNotBeFound("uvuA.notEquals=" + DEFAULT_UVU_A);

        // Get all the gameList where uvuA not equals to UPDATED_UVU_A
        defaultGameShouldBeFound("uvuA.notEquals=" + UPDATED_UVU_A);
    }

    @Test
    @Transactional
    void getAllGamesByUvuAIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuA in DEFAULT_UVU_A or UPDATED_UVU_A
        defaultGameShouldBeFound("uvuA.in=" + DEFAULT_UVU_A + "," + UPDATED_UVU_A);

        // Get all the gameList where uvuA equals to UPDATED_UVU_A
        defaultGameShouldNotBeFound("uvuA.in=" + UPDATED_UVU_A);
    }

    @Test
    @Transactional
    void getAllGamesByUvuAIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuA is not null
        defaultGameShouldBeFound("uvuA.specified=true");

        // Get all the gameList where uvuA is null
        defaultGameShouldNotBeFound("uvuA.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByUvuAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuA is greater than or equal to DEFAULT_UVU_A
        defaultGameShouldBeFound("uvuA.greaterThanOrEqual=" + DEFAULT_UVU_A);

        // Get all the gameList where uvuA is greater than or equal to UPDATED_UVU_A
        defaultGameShouldNotBeFound("uvuA.greaterThanOrEqual=" + UPDATED_UVU_A);
    }

    @Test
    @Transactional
    void getAllGamesByUvuAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuA is less than or equal to DEFAULT_UVU_A
        defaultGameShouldBeFound("uvuA.lessThanOrEqual=" + DEFAULT_UVU_A);

        // Get all the gameList where uvuA is less than or equal to SMALLER_UVU_A
        defaultGameShouldNotBeFound("uvuA.lessThanOrEqual=" + SMALLER_UVU_A);
    }

    @Test
    @Transactional
    void getAllGamesByUvuAIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuA is less than DEFAULT_UVU_A
        defaultGameShouldNotBeFound("uvuA.lessThan=" + DEFAULT_UVU_A);

        // Get all the gameList where uvuA is less than UPDATED_UVU_A
        defaultGameShouldBeFound("uvuA.lessThan=" + UPDATED_UVU_A);
    }

    @Test
    @Transactional
    void getAllGamesByUvuAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuA is greater than DEFAULT_UVU_A
        defaultGameShouldNotBeFound("uvuA.greaterThan=" + DEFAULT_UVU_A);

        // Get all the gameList where uvuA is greater than SMALLER_UVU_A
        defaultGameShouldBeFound("uvuA.greaterThan=" + SMALLER_UVU_A);
    }

    @Test
    @Transactional
    void getAllGamesByUvuBIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuB equals to DEFAULT_UVU_B
        defaultGameShouldBeFound("uvuB.equals=" + DEFAULT_UVU_B);

        // Get all the gameList where uvuB equals to UPDATED_UVU_B
        defaultGameShouldNotBeFound("uvuB.equals=" + UPDATED_UVU_B);
    }

    @Test
    @Transactional
    void getAllGamesByUvuBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuB not equals to DEFAULT_UVU_B
        defaultGameShouldNotBeFound("uvuB.notEquals=" + DEFAULT_UVU_B);

        // Get all the gameList where uvuB not equals to UPDATED_UVU_B
        defaultGameShouldBeFound("uvuB.notEquals=" + UPDATED_UVU_B);
    }

    @Test
    @Transactional
    void getAllGamesByUvuBIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuB in DEFAULT_UVU_B or UPDATED_UVU_B
        defaultGameShouldBeFound("uvuB.in=" + DEFAULT_UVU_B + "," + UPDATED_UVU_B);

        // Get all the gameList where uvuB equals to UPDATED_UVU_B
        defaultGameShouldNotBeFound("uvuB.in=" + UPDATED_UVU_B);
    }

    @Test
    @Transactional
    void getAllGamesByUvuBIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuB is not null
        defaultGameShouldBeFound("uvuB.specified=true");

        // Get all the gameList where uvuB is null
        defaultGameShouldNotBeFound("uvuB.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByUvuBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuB is greater than or equal to DEFAULT_UVU_B
        defaultGameShouldBeFound("uvuB.greaterThanOrEqual=" + DEFAULT_UVU_B);

        // Get all the gameList where uvuB is greater than or equal to UPDATED_UVU_B
        defaultGameShouldNotBeFound("uvuB.greaterThanOrEqual=" + UPDATED_UVU_B);
    }

    @Test
    @Transactional
    void getAllGamesByUvuBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuB is less than or equal to DEFAULT_UVU_B
        defaultGameShouldBeFound("uvuB.lessThanOrEqual=" + DEFAULT_UVU_B);

        // Get all the gameList where uvuB is less than or equal to SMALLER_UVU_B
        defaultGameShouldNotBeFound("uvuB.lessThanOrEqual=" + SMALLER_UVU_B);
    }

    @Test
    @Transactional
    void getAllGamesByUvuBIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuB is less than DEFAULT_UVU_B
        defaultGameShouldNotBeFound("uvuB.lessThan=" + DEFAULT_UVU_B);

        // Get all the gameList where uvuB is less than UPDATED_UVU_B
        defaultGameShouldBeFound("uvuB.lessThan=" + UPDATED_UVU_B);
    }

    @Test
    @Transactional
    void getAllGamesByUvuBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where uvuB is greater than DEFAULT_UVU_B
        defaultGameShouldNotBeFound("uvuB.greaterThan=" + DEFAULT_UVU_B);

        // Get all the gameList where uvuB is greater than SMALLER_UVU_B
        defaultGameShouldBeFound("uvuB.greaterThan=" + SMALLER_UVU_B);
    }

    @Test
    @Transactional
    void getAllGamesByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where group equals to DEFAULT_GROUP
        defaultGameShouldBeFound("group.equals=" + DEFAULT_GROUP);

        // Get all the gameList where group equals to UPDATED_GROUP
        defaultGameShouldNotBeFound("group.equals=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    void getAllGamesByGroupIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where group not equals to DEFAULT_GROUP
        defaultGameShouldNotBeFound("group.notEquals=" + DEFAULT_GROUP);

        // Get all the gameList where group not equals to UPDATED_GROUP
        defaultGameShouldBeFound("group.notEquals=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    void getAllGamesByGroupIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where group in DEFAULT_GROUP or UPDATED_GROUP
        defaultGameShouldBeFound("group.in=" + DEFAULT_GROUP + "," + UPDATED_GROUP);

        // Get all the gameList where group equals to UPDATED_GROUP
        defaultGameShouldNotBeFound("group.in=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    void getAllGamesByGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where group is not null
        defaultGameShouldBeFound("group.specified=true");

        // Get all the gameList where group is null
        defaultGameShouldNotBeFound("group.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByGroupIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where group is greater than or equal to DEFAULT_GROUP
        defaultGameShouldBeFound("group.greaterThanOrEqual=" + DEFAULT_GROUP);

        // Get all the gameList where group is greater than or equal to UPDATED_GROUP
        defaultGameShouldNotBeFound("group.greaterThanOrEqual=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    void getAllGamesByGroupIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where group is less than or equal to DEFAULT_GROUP
        defaultGameShouldBeFound("group.lessThanOrEqual=" + DEFAULT_GROUP);

        // Get all the gameList where group is less than or equal to SMALLER_GROUP
        defaultGameShouldNotBeFound("group.lessThanOrEqual=" + SMALLER_GROUP);
    }

    @Test
    @Transactional
    void getAllGamesByGroupIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where group is less than DEFAULT_GROUP
        defaultGameShouldNotBeFound("group.lessThan=" + DEFAULT_GROUP);

        // Get all the gameList where group is less than UPDATED_GROUP
        defaultGameShouldBeFound("group.lessThan=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    void getAllGamesByGroupIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where group is greater than DEFAULT_GROUP
        defaultGameShouldNotBeFound("group.greaterThan=" + DEFAULT_GROUP);

        // Get all the gameList where group is greater than SMALLER_GROUP
        defaultGameShouldBeFound("group.greaterThan=" + SMALLER_GROUP);
    }

    @Test
    @Transactional
    void getAllGamesByClasifIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif equals to DEFAULT_CLASIF
        defaultGameShouldBeFound("clasif.equals=" + DEFAULT_CLASIF);

        // Get all the gameList where clasif equals to UPDATED_CLASIF
        defaultGameShouldNotBeFound("clasif.equals=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesByClasifIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif not equals to DEFAULT_CLASIF
        defaultGameShouldNotBeFound("clasif.notEquals=" + DEFAULT_CLASIF);

        // Get all the gameList where clasif not equals to UPDATED_CLASIF
        defaultGameShouldBeFound("clasif.notEquals=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesByClasifIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif in DEFAULT_CLASIF or UPDATED_CLASIF
        defaultGameShouldBeFound("clasif.in=" + DEFAULT_CLASIF + "," + UPDATED_CLASIF);

        // Get all the gameList where clasif equals to UPDATED_CLASIF
        defaultGameShouldNotBeFound("clasif.in=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesByClasifIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif is not null
        defaultGameShouldBeFound("clasif.specified=true");

        // Get all the gameList where clasif is null
        defaultGameShouldNotBeFound("clasif.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByClasifContainsSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif contains DEFAULT_CLASIF
        defaultGameShouldBeFound("clasif.contains=" + DEFAULT_CLASIF);

        // Get all the gameList where clasif contains UPDATED_CLASIF
        defaultGameShouldNotBeFound("clasif.contains=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesByClasifNotContainsSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where clasif does not contain DEFAULT_CLASIF
        defaultGameShouldNotBeFound("clasif.doesNotContain=" + DEFAULT_CLASIF);

        // Get all the gameList where clasif does not contain UPDATED_CLASIF
        defaultGameShouldBeFound("clasif.doesNotContain=" + UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void getAllGamesByTeamAIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        Team teamA = TeamResourceIT.createEntity(em);
        em.persist(teamA);
        em.flush();
        game.setTeamA(teamA);
        gameRepository.saveAndFlush(game);
        Long teamAId = teamA.getId();

        // Get all the gameList where teamA equals to teamAId
        defaultGameShouldBeFound("teamAId.equals=" + teamAId);

        // Get all the gameList where teamA equals to (teamAId + 1)
        defaultGameShouldNotBeFound("teamAId.equals=" + (teamAId + 1));
    }

    @Test
    @Transactional
    void getAllGamesByTeamBIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        Team teamB = TeamResourceIT.createEntity(em);
        em.persist(teamB);
        em.flush();
        game.setTeamB(teamB);
        gameRepository.saveAndFlush(game);
        Long teamBId = teamB.getId();

        // Get all the gameList where teamB equals to teamBId
        defaultGameShouldBeFound("teamBId.equals=" + teamBId);

        // Get all the gameList where teamB equals to (teamBId + 1)
        defaultGameShouldNotBeFound("teamBId.equals=" + (teamBId + 1));
    }

    @Test
    @Transactional
    void getAllGamesByEventCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        EventCategory eventCategory = EventCategoryResourceIT.createEntity(em);
        em.persist(eventCategory);
        em.flush();
        game.setEventCategory(eventCategory);
        gameRepository.saveAndFlush(game);
        Long eventCategoryId = eventCategory.getId();

        // Get all the gameList where eventCategory equals to eventCategoryId
        defaultGameShouldBeFound("eventCategoryId.equals=" + eventCategoryId);

        // Get all the gameList where eventCategory equals to (eventCategoryId + 1)
        defaultGameShouldNotBeFound("eventCategoryId.equals=" + (eventCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGameShouldBeFound(String filter) throws Exception {
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].pointsA").value(hasItem(DEFAULT_POINTS_A)))
            .andExpect(jsonPath("$.[*].pointsB").value(hasItem(DEFAULT_POINTS_B)))
            .andExpect(jsonPath("$.[*].splitDeckNum").value(hasItem(DEFAULT_SPLIT_DECK_NUM)))
            .andExpect(jsonPath("$.[*].timeLeft").value(hasItem(DEFAULT_TIME_LEFT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].overtimeA").value(hasItem(DEFAULT_OVERTIME_A)))
            .andExpect(jsonPath("$.[*].overtimeB").value(hasItem(DEFAULT_OVERTIME_B)))
            .andExpect(jsonPath("$.[*].uvuA").value(hasItem(DEFAULT_UVU_A)))
            .andExpect(jsonPath("$.[*].uvuB").value(hasItem(DEFAULT_UVU_B)))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP)))
            .andExpect(jsonPath("$.[*].clasif").value(hasItem(DEFAULT_CLASIF)));

        // Check, that the count call also returns 1
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGameShouldNotBeFound(String filter) throws Exception {
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game
        Game updatedGame = gameRepository.findById(game.getId()).get();
        // Disconnect from session so that the updates on updatedGame are not directly saved in db
        em.detach(updatedGame);
        updatedGame
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .splitDeckNum(UPDATED_SPLIT_DECK_NUM)
            .timeLeft(UPDATED_TIME_LEFT)
            .status(UPDATED_STATUS)
            .overtimeA(UPDATED_OVERTIME_A)
            .overtimeB(UPDATED_OVERTIME_B)
            .uvuA(UPDATED_UVU_A)
            .uvuB(UPDATED_UVU_B)
            .group(UPDATED_GROUP)
            .clasif(UPDATED_CLASIF);
        GameDTO gameDTO = gameMapper.toDto(updatedGame);

        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getPointsA()).isEqualTo(UPDATED_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(UPDATED_POINTS_B);
        assertThat(testGame.getSplitDeckNum()).isEqualTo(UPDATED_SPLIT_DECK_NUM);
        assertThat(testGame.getTimeLeft()).isEqualTo(UPDATED_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGame.getOvertimeA()).isEqualTo(UPDATED_OVERTIME_A);
        assertThat(testGame.getOvertimeB()).isEqualTo(UPDATED_OVERTIME_B);
        assertThat(testGame.getUvuA()).isEqualTo(UPDATED_UVU_A);
        assertThat(testGame.getUvuB()).isEqualTo(UPDATED_UVU_B);
        assertThat(testGame.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testGame.getClasif()).isEqualTo(UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void putNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameWithPatch() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game using partial update
        Game partialUpdatedGame = new Game();
        partialUpdatedGame.setId(game.getId());

        partialUpdatedGame
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .timeLeft(UPDATED_TIME_LEFT)
            .status(UPDATED_STATUS)
            .overtimeA(UPDATED_OVERTIME_A)
            .uvuA(UPDATED_UVU_A)
            .uvuB(UPDATED_UVU_B)
            .clasif(UPDATED_CLASIF);

        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGame))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getPointsA()).isEqualTo(UPDATED_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(UPDATED_POINTS_B);
        assertThat(testGame.getSplitDeckNum()).isEqualTo(DEFAULT_SPLIT_DECK_NUM);
        assertThat(testGame.getTimeLeft()).isEqualTo(UPDATED_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGame.getOvertimeA()).isEqualTo(UPDATED_OVERTIME_A);
        assertThat(testGame.getOvertimeB()).isEqualTo(DEFAULT_OVERTIME_B);
        assertThat(testGame.getUvuA()).isEqualTo(UPDATED_UVU_A);
        assertThat(testGame.getUvuB()).isEqualTo(UPDATED_UVU_B);
        assertThat(testGame.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testGame.getClasif()).isEqualTo(UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void fullUpdateGameWithPatch() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game using partial update
        Game partialUpdatedGame = new Game();
        partialUpdatedGame.setId(game.getId());

        partialUpdatedGame
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .splitDeckNum(UPDATED_SPLIT_DECK_NUM)
            .timeLeft(UPDATED_TIME_LEFT)
            .status(UPDATED_STATUS)
            .overtimeA(UPDATED_OVERTIME_A)
            .overtimeB(UPDATED_OVERTIME_B)
            .uvuA(UPDATED_UVU_A)
            .uvuB(UPDATED_UVU_B)
            .group(UPDATED_GROUP)
            .clasif(UPDATED_CLASIF);

        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGame))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getPointsA()).isEqualTo(UPDATED_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(UPDATED_POINTS_B);
        assertThat(testGame.getSplitDeckNum()).isEqualTo(UPDATED_SPLIT_DECK_NUM);
        assertThat(testGame.getTimeLeft()).isEqualTo(UPDATED_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGame.getOvertimeA()).isEqualTo(UPDATED_OVERTIME_A);
        assertThat(testGame.getOvertimeB()).isEqualTo(UPDATED_OVERTIME_B);
        assertThat(testGame.getUvuA()).isEqualTo(UPDATED_UVU_A);
        assertThat(testGame.getUvuB()).isEqualTo(UPDATED_UVU_B);
        assertThat(testGame.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testGame.getClasif()).isEqualTo(UPDATED_CLASIF);
    }

    @Test
    @Transactional
    void patchNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeDelete = gameRepository.findAll().size();

        // Delete the game
        restGameMockMvc
            .perform(delete(ENTITY_API_URL_ID, game.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
