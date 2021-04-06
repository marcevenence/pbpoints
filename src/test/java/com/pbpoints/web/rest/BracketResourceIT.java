package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Bracket;
import com.pbpoints.repository.BracketRepository;
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
 * Integration tests for the {@link BracketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BracketResourceIT {

    private static final Integer DEFAULT_TEAMS = 1;
    private static final Integer UPDATED_TEAMS = 2;

    private static final Integer DEFAULT_TEAMS_5_A = 1;
    private static final Integer UPDATED_TEAMS_5_A = 2;

    private static final Integer DEFAULT_TEAMS_5_B = 1;
    private static final Integer UPDATED_TEAMS_5_B = 2;

    private static final Integer DEFAULT_TEAMS_6_A = 1;
    private static final Integer UPDATED_TEAMS_6_A = 2;

    private static final Integer DEFAULT_TEAMS_6_B = 1;
    private static final Integer UPDATED_TEAMS_6_B = 2;

    private static final String ENTITY_API_URL = "/api/brackets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BracketRepository bracketRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBracketMockMvc;

    private Bracket bracket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bracket createEntity(EntityManager em) {
        Bracket bracket = new Bracket()
            .teams(DEFAULT_TEAMS)
            .teams5A(DEFAULT_TEAMS_5_A)
            .teams5B(DEFAULT_TEAMS_5_B)
            .teams6A(DEFAULT_TEAMS_6_A)
            .teams6B(DEFAULT_TEAMS_6_B);
        return bracket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bracket createUpdatedEntity(EntityManager em) {
        Bracket bracket = new Bracket()
            .teams(UPDATED_TEAMS)
            .teams5A(UPDATED_TEAMS_5_A)
            .teams5B(UPDATED_TEAMS_5_B)
            .teams6A(UPDATED_TEAMS_6_A)
            .teams6B(UPDATED_TEAMS_6_B);
        return bracket;
    }

    @BeforeEach
    public void initTest() {
        bracket = createEntity(em);
    }

    @Test
    @Transactional
    void createBracket() throws Exception {
        int databaseSizeBeforeCreate = bracketRepository.findAll().size();
        // Create the Bracket
        restBracketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bracket)))
            .andExpect(status().isCreated());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeCreate + 1);
        Bracket testBracket = bracketList.get(bracketList.size() - 1);
        assertThat(testBracket.getTeams()).isEqualTo(DEFAULT_TEAMS);
        assertThat(testBracket.getTeams5A()).isEqualTo(DEFAULT_TEAMS_5_A);
        assertThat(testBracket.getTeams5B()).isEqualTo(DEFAULT_TEAMS_5_B);
        assertThat(testBracket.getTeams6A()).isEqualTo(DEFAULT_TEAMS_6_A);
        assertThat(testBracket.getTeams6B()).isEqualTo(DEFAULT_TEAMS_6_B);
    }

    @Test
    @Transactional
    void createBracketWithExistingId() throws Exception {
        // Create the Bracket with an existing ID
        bracket.setId(1L);

        int databaseSizeBeforeCreate = bracketRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBracketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bracket)))
            .andExpect(status().isBadRequest());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTeamsIsRequired() throws Exception {
        int databaseSizeBeforeTest = bracketRepository.findAll().size();
        // set the field null
        bracket.setTeams(null);

        // Create the Bracket, which fails.

        restBracketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bracket)))
            .andExpect(status().isBadRequest());

        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTeams5AIsRequired() throws Exception {
        int databaseSizeBeforeTest = bracketRepository.findAll().size();
        // set the field null
        bracket.setTeams5A(null);

        // Create the Bracket, which fails.

        restBracketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bracket)))
            .andExpect(status().isBadRequest());

        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTeams5BIsRequired() throws Exception {
        int databaseSizeBeforeTest = bracketRepository.findAll().size();
        // set the field null
        bracket.setTeams5B(null);

        // Create the Bracket, which fails.

        restBracketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bracket)))
            .andExpect(status().isBadRequest());

        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTeams6AIsRequired() throws Exception {
        int databaseSizeBeforeTest = bracketRepository.findAll().size();
        // set the field null
        bracket.setTeams6A(null);

        // Create the Bracket, which fails.

        restBracketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bracket)))
            .andExpect(status().isBadRequest());

        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTeams6BIsRequired() throws Exception {
        int databaseSizeBeforeTest = bracketRepository.findAll().size();
        // set the field null
        bracket.setTeams6B(null);

        // Create the Bracket, which fails.

        restBracketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bracket)))
            .andExpect(status().isBadRequest());

        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBrackets() throws Exception {
        // Initialize the database
        bracketRepository.saveAndFlush(bracket);

        // Get all the bracketList
        restBracketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bracket.getId().intValue())))
            .andExpect(jsonPath("$.[*].teams").value(hasItem(DEFAULT_TEAMS)))
            .andExpect(jsonPath("$.[*].teams5A").value(hasItem(DEFAULT_TEAMS_5_A)))
            .andExpect(jsonPath("$.[*].teams5B").value(hasItem(DEFAULT_TEAMS_5_B)))
            .andExpect(jsonPath("$.[*].teams6A").value(hasItem(DEFAULT_TEAMS_6_A)))
            .andExpect(jsonPath("$.[*].teams6B").value(hasItem(DEFAULT_TEAMS_6_B)));
    }

    @Test
    @Transactional
    void getBracket() throws Exception {
        // Initialize the database
        bracketRepository.saveAndFlush(bracket);

        // Get the bracket
        restBracketMockMvc
            .perform(get(ENTITY_API_URL_ID, bracket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bracket.getId().intValue()))
            .andExpect(jsonPath("$.teams").value(DEFAULT_TEAMS))
            .andExpect(jsonPath("$.teams5A").value(DEFAULT_TEAMS_5_A))
            .andExpect(jsonPath("$.teams5B").value(DEFAULT_TEAMS_5_B))
            .andExpect(jsonPath("$.teams6A").value(DEFAULT_TEAMS_6_A))
            .andExpect(jsonPath("$.teams6B").value(DEFAULT_TEAMS_6_B));
    }

    @Test
    @Transactional
    void getNonExistingBracket() throws Exception {
        // Get the bracket
        restBracketMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBracket() throws Exception {
        // Initialize the database
        bracketRepository.saveAndFlush(bracket);

        int databaseSizeBeforeUpdate = bracketRepository.findAll().size();

        // Update the bracket
        Bracket updatedBracket = bracketRepository.findById(bracket.getId()).get();
        // Disconnect from session so that the updates on updatedBracket are not directly saved in db
        em.detach(updatedBracket);
        updatedBracket
            .teams(UPDATED_TEAMS)
            .teams5A(UPDATED_TEAMS_5_A)
            .teams5B(UPDATED_TEAMS_5_B)
            .teams6A(UPDATED_TEAMS_6_A)
            .teams6B(UPDATED_TEAMS_6_B);

        restBracketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBracket.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBracket))
            )
            .andExpect(status().isOk());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeUpdate);
        Bracket testBracket = bracketList.get(bracketList.size() - 1);
        assertThat(testBracket.getTeams()).isEqualTo(UPDATED_TEAMS);
        assertThat(testBracket.getTeams5A()).isEqualTo(UPDATED_TEAMS_5_A);
        assertThat(testBracket.getTeams5B()).isEqualTo(UPDATED_TEAMS_5_B);
        assertThat(testBracket.getTeams6A()).isEqualTo(UPDATED_TEAMS_6_A);
        assertThat(testBracket.getTeams6B()).isEqualTo(UPDATED_TEAMS_6_B);
    }

    @Test
    @Transactional
    void putNonExistingBracket() throws Exception {
        int databaseSizeBeforeUpdate = bracketRepository.findAll().size();
        bracket.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBracketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bracket.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bracket))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBracket() throws Exception {
        int databaseSizeBeforeUpdate = bracketRepository.findAll().size();
        bracket.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBracketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bracket))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBracket() throws Exception {
        int databaseSizeBeforeUpdate = bracketRepository.findAll().size();
        bracket.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBracketMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bracket)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBracketWithPatch() throws Exception {
        // Initialize the database
        bracketRepository.saveAndFlush(bracket);

        int databaseSizeBeforeUpdate = bracketRepository.findAll().size();

        // Update the bracket using partial update
        Bracket partialUpdatedBracket = new Bracket();
        partialUpdatedBracket.setId(bracket.getId());

        partialUpdatedBracket.teams(UPDATED_TEAMS).teams5B(UPDATED_TEAMS_5_B).teams6A(UPDATED_TEAMS_6_A).teams6B(UPDATED_TEAMS_6_B);

        restBracketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBracket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBracket))
            )
            .andExpect(status().isOk());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeUpdate);
        Bracket testBracket = bracketList.get(bracketList.size() - 1);
        assertThat(testBracket.getTeams()).isEqualTo(UPDATED_TEAMS);
        assertThat(testBracket.getTeams5A()).isEqualTo(DEFAULT_TEAMS_5_A);
        assertThat(testBracket.getTeams5B()).isEqualTo(UPDATED_TEAMS_5_B);
        assertThat(testBracket.getTeams6A()).isEqualTo(UPDATED_TEAMS_6_A);
        assertThat(testBracket.getTeams6B()).isEqualTo(UPDATED_TEAMS_6_B);
    }

    @Test
    @Transactional
    void fullUpdateBracketWithPatch() throws Exception {
        // Initialize the database
        bracketRepository.saveAndFlush(bracket);

        int databaseSizeBeforeUpdate = bracketRepository.findAll().size();

        // Update the bracket using partial update
        Bracket partialUpdatedBracket = new Bracket();
        partialUpdatedBracket.setId(bracket.getId());

        partialUpdatedBracket
            .teams(UPDATED_TEAMS)
            .teams5A(UPDATED_TEAMS_5_A)
            .teams5B(UPDATED_TEAMS_5_B)
            .teams6A(UPDATED_TEAMS_6_A)
            .teams6B(UPDATED_TEAMS_6_B);

        restBracketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBracket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBracket))
            )
            .andExpect(status().isOk());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeUpdate);
        Bracket testBracket = bracketList.get(bracketList.size() - 1);
        assertThat(testBracket.getTeams()).isEqualTo(UPDATED_TEAMS);
        assertThat(testBracket.getTeams5A()).isEqualTo(UPDATED_TEAMS_5_A);
        assertThat(testBracket.getTeams5B()).isEqualTo(UPDATED_TEAMS_5_B);
        assertThat(testBracket.getTeams6A()).isEqualTo(UPDATED_TEAMS_6_A);
        assertThat(testBracket.getTeams6B()).isEqualTo(UPDATED_TEAMS_6_B);
    }

    @Test
    @Transactional
    void patchNonExistingBracket() throws Exception {
        int databaseSizeBeforeUpdate = bracketRepository.findAll().size();
        bracket.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBracketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bracket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bracket))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBracket() throws Exception {
        int databaseSizeBeforeUpdate = bracketRepository.findAll().size();
        bracket.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBracketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bracket))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBracket() throws Exception {
        int databaseSizeBeforeUpdate = bracketRepository.findAll().size();
        bracket.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBracketMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bracket)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bracket in the database
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBracket() throws Exception {
        // Initialize the database
        bracketRepository.saveAndFlush(bracket);

        int databaseSizeBeforeDelete = bracketRepository.findAll().size();

        // Delete the bracket
        restBracketMockMvc
            .perform(delete(ENTITY_API_URL_ID, bracket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bracket> bracketList = bracketRepository.findAll();
        assertThat(bracketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
