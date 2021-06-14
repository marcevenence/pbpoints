package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Category;
import com.pbpoints.domain.Event;
import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Format;
import com.pbpoints.domain.Game;
import com.pbpoints.domain.Roster;
import com.pbpoints.repository.EventCategoryRepository;
import com.pbpoints.service.criteria.EventCategoryCriteria;
import com.pbpoints.service.dto.EventCategoryDTO;
import com.pbpoints.service.mapper.EventCategoryMapper;
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
 * Integration tests for the {@link EventCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventCategoryResourceIT {

    private static final Boolean DEFAULT_SPLIT_DECK = false;
    private static final Boolean UPDATED_SPLIT_DECK = true;

    private static final String ENTITY_API_URL = "/api/event-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private EventCategoryMapper eventCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventCategoryMockMvc;

    private EventCategory eventCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventCategory createEntity(EntityManager em) {
        EventCategory eventCategory = new EventCategory().splitDeck(DEFAULT_SPLIT_DECK);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        eventCategory.setEvent(event);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        eventCategory.setCategory(category);
        // Add required entity
        Format format;
        if (TestUtil.findAll(em, Format.class).isEmpty()) {
            format = FormatResourceIT.createEntity(em);
            em.persist(format);
            em.flush();
        } else {
            format = TestUtil.findAll(em, Format.class).get(0);
        }
        eventCategory.setFormat(format);
        return eventCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventCategory createUpdatedEntity(EntityManager em) {
        EventCategory eventCategory = new EventCategory().splitDeck(UPDATED_SPLIT_DECK);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        eventCategory.setEvent(event);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        eventCategory.setCategory(category);
        // Add required entity
        Format format;
        if (TestUtil.findAll(em, Format.class).isEmpty()) {
            format = FormatResourceIT.createUpdatedEntity(em);
            em.persist(format);
            em.flush();
        } else {
            format = TestUtil.findAll(em, Format.class).get(0);
        }
        eventCategory.setFormat(format);
        return eventCategory;
    }

    @BeforeEach
    public void initTest() {
        eventCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createEventCategory() throws Exception {
        int databaseSizeBeforeCreate = eventCategoryRepository.findAll().size();
        // Create the EventCategory
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);
        restEventCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        EventCategory testEventCategory = eventCategoryList.get(eventCategoryList.size() - 1);
        assertThat(testEventCategory.getSplitDeck()).isEqualTo(DEFAULT_SPLIT_DECK);
    }

    @Test
    @Transactional
    void createEventCategoryWithExistingId() throws Exception {
        // Create the EventCategory with an existing ID
        eventCategory.setId(1L);
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);

        int databaseSizeBeforeCreate = eventCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEventCategories() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList
        restEventCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].splitDeck").value(hasItem(DEFAULT_SPLIT_DECK.booleanValue())));
    }

    @Test
    @Transactional
    void getEventCategory() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get the eventCategory
        restEventCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, eventCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventCategory.getId().intValue()))
            .andExpect(jsonPath("$.splitDeck").value(DEFAULT_SPLIT_DECK.booleanValue()));
    }

    @Test
    @Transactional
    void getEventCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        Long id = eventCategory.getId();

        defaultEventCategoryShouldBeFound("id.equals=" + id);
        defaultEventCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultEventCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultEventCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventCategoriesBySplitDeckIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList where splitDeck equals to DEFAULT_SPLIT_DECK
        defaultEventCategoryShouldBeFound("splitDeck.equals=" + DEFAULT_SPLIT_DECK);

        // Get all the eventCategoryList where splitDeck equals to UPDATED_SPLIT_DECK
        defaultEventCategoryShouldNotBeFound("splitDeck.equals=" + UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    void getAllEventCategoriesBySplitDeckIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList where splitDeck not equals to DEFAULT_SPLIT_DECK
        defaultEventCategoryShouldNotBeFound("splitDeck.notEquals=" + DEFAULT_SPLIT_DECK);

        // Get all the eventCategoryList where splitDeck not equals to UPDATED_SPLIT_DECK
        defaultEventCategoryShouldBeFound("splitDeck.notEquals=" + UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    void getAllEventCategoriesBySplitDeckIsInShouldWork() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList where splitDeck in DEFAULT_SPLIT_DECK or UPDATED_SPLIT_DECK
        defaultEventCategoryShouldBeFound("splitDeck.in=" + DEFAULT_SPLIT_DECK + "," + UPDATED_SPLIT_DECK);

        // Get all the eventCategoryList where splitDeck equals to UPDATED_SPLIT_DECK
        defaultEventCategoryShouldNotBeFound("splitDeck.in=" + UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    void getAllEventCategoriesBySplitDeckIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList where splitDeck is not null
        defaultEventCategoryShouldBeFound("splitDeck.specified=true");

        // Get all the eventCategoryList where splitDeck is null
        defaultEventCategoryShouldNotBeFound("splitDeck.specified=false");
    }

    @Test
    @Transactional
    void getAllEventCategoriesByGameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);
        Game game = GameResourceIT.createEntity(em);
        em.persist(game);
        em.flush();
        eventCategory.addGame(game);
        eventCategoryRepository.saveAndFlush(eventCategory);
        Long gameId = game.getId();

        // Get all the eventCategoryList where game equals to gameId
        defaultEventCategoryShouldBeFound("gameId.equals=" + gameId);

        // Get all the eventCategoryList where game equals to (gameId + 1)
        defaultEventCategoryShouldNotBeFound("gameId.equals=" + (gameId + 1));
    }

    @Test
    @Transactional
    void getAllEventCategoriesByRosterIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);
        Roster roster = RosterResourceIT.createEntity(em);
        em.persist(roster);
        em.flush();
        eventCategory.addRoster(roster);
        eventCategoryRepository.saveAndFlush(eventCategory);
        Long rosterId = roster.getId();

        // Get all the eventCategoryList where roster equals to rosterId
        defaultEventCategoryShouldBeFound("rosterId.equals=" + rosterId);

        // Get all the eventCategoryList where roster equals to (rosterId + 1)
        defaultEventCategoryShouldNotBeFound("rosterId.equals=" + (rosterId + 1));
    }

    @Test
    @Transactional
    void getAllEventCategoriesByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);
        Event event = EventResourceIT.createEntity(em);
        em.persist(event);
        em.flush();
        eventCategory.setEvent(event);
        eventCategoryRepository.saveAndFlush(eventCategory);
        Long eventId = event.getId();

        // Get all the eventCategoryList where event equals to eventId
        defaultEventCategoryShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventCategoryList where event equals to (eventId + 1)
        defaultEventCategoryShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllEventCategoriesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        eventCategory.setCategory(category);
        eventCategoryRepository.saveAndFlush(eventCategory);
        Long categoryId = category.getId();

        // Get all the eventCategoryList where category equals to categoryId
        defaultEventCategoryShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the eventCategoryList where category equals to (categoryId + 1)
        defaultEventCategoryShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllEventCategoriesByFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);
        Format format = FormatResourceIT.createEntity(em);
        em.persist(format);
        em.flush();
        eventCategory.setFormat(format);
        eventCategoryRepository.saveAndFlush(eventCategory);
        Long formatId = format.getId();

        // Get all the eventCategoryList where format equals to formatId
        defaultEventCategoryShouldBeFound("formatId.equals=" + formatId);

        // Get all the eventCategoryList where format equals to (formatId + 1)
        defaultEventCategoryShouldNotBeFound("formatId.equals=" + (formatId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventCategoryShouldBeFound(String filter) throws Exception {
        restEventCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].splitDeck").value(hasItem(DEFAULT_SPLIT_DECK.booleanValue())));

        // Check, that the count call also returns 1
        restEventCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventCategoryShouldNotBeFound(String filter) throws Exception {
        restEventCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventCategory() throws Exception {
        // Get the eventCategory
        restEventCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEventCategory() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();

        // Update the eventCategory
        EventCategory updatedEventCategory = eventCategoryRepository.findById(eventCategory.getId()).get();
        // Disconnect from session so that the updates on updatedEventCategory are not directly saved in db
        em.detach(updatedEventCategory);
        updatedEventCategory.splitDeck(UPDATED_SPLIT_DECK);
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(updatedEventCategory);

        restEventCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
        EventCategory testEventCategory = eventCategoryList.get(eventCategoryList.size() - 1);
        assertThat(testEventCategory.getSplitDeck()).isEqualTo(UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    void putNonExistingEventCategory() throws Exception {
        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();
        eventCategory.setId(count.incrementAndGet());

        // Create the EventCategory
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventCategory() throws Exception {
        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();
        eventCategory.setId(count.incrementAndGet());

        // Create the EventCategory
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventCategory() throws Exception {
        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();
        eventCategory.setId(count.incrementAndGet());

        // Create the EventCategory
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventCategoryWithPatch() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();

        // Update the eventCategory using partial update
        EventCategory partialUpdatedEventCategory = new EventCategory();
        partialUpdatedEventCategory.setId(eventCategory.getId());

        partialUpdatedEventCategory.splitDeck(UPDATED_SPLIT_DECK);

        restEventCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventCategory))
            )
            .andExpect(status().isOk());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
        EventCategory testEventCategory = eventCategoryList.get(eventCategoryList.size() - 1);
        assertThat(testEventCategory.getSplitDeck()).isEqualTo(UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    void fullUpdateEventCategoryWithPatch() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();

        // Update the eventCategory using partial update
        EventCategory partialUpdatedEventCategory = new EventCategory();
        partialUpdatedEventCategory.setId(eventCategory.getId());

        partialUpdatedEventCategory.splitDeck(UPDATED_SPLIT_DECK);

        restEventCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventCategory))
            )
            .andExpect(status().isOk());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
        EventCategory testEventCategory = eventCategoryList.get(eventCategoryList.size() - 1);
        assertThat(testEventCategory.getSplitDeck()).isEqualTo(UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    void patchNonExistingEventCategory() throws Exception {
        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();
        eventCategory.setId(count.incrementAndGet());

        // Create the EventCategory
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventCategory() throws Exception {
        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();
        eventCategory.setId(count.incrementAndGet());

        // Create the EventCategory
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventCategory() throws Exception {
        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();
        eventCategory.setId(count.incrementAndGet());

        // Create the EventCategory
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventCategory() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        int databaseSizeBeforeDelete = eventCategoryRepository.findAll().size();

        // Delete the eventCategory
        restEventCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
