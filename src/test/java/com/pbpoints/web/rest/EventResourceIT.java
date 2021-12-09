package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Event;
import com.pbpoints.domain.Field;
import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.enumeration.Status;
import com.pbpoints.repository.EventRepository;
import com.pbpoints.service.criteria.EventCriteria;
import com.pbpoints.service.dto.EventDTO;
import com.pbpoints.service.mapper.EventMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link EventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FROM_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_INSCRIPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_INSCRIPTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_INSCRIPTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Status DEFAULT_STATUS = Status.CREATED;
    private static final Status UPDATED_STATUS = Status.PENDING;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_END_INSCRIPTION_PLAYERS_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_INSCRIPTION_PLAYERS_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_INSCRIPTION_PLAYERS_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .name(DEFAULT_NAME)
            .fromDate(DEFAULT_FROM_DATE)
            .endDate(DEFAULT_END_DATE)
            .endInscriptionDate(DEFAULT_END_INSCRIPTION_DATE)
            .status(DEFAULT_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE)
            .endInscriptionPlayersDate(DEFAULT_END_INSCRIPTION_PLAYERS_DATE);
        // Add required entity
        Field field;
        if (TestUtil.findAll(em, Field.class).isEmpty()) {
            field = FieldResourceIT.createEntity(em);
            em.persist(field);
            em.flush();
        } else {
            field = TestUtil.findAll(em, Field.class).get(0);
        }
        event.setField(field);
        return event;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event event = new Event()
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .endDate(UPDATED_END_DATE)
            .endInscriptionDate(UPDATED_END_INSCRIPTION_DATE)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .endInscriptionPlayersDate(UPDATED_END_INSCRIPTION_PLAYERS_DATE);
        // Add required entity
        Field field;
        if (TestUtil.findAll(em, Field.class).isEmpty()) {
            field = FieldResourceIT.createUpdatedEntity(em);
            em.persist(field);
            em.flush();
        } else {
            field = TestUtil.findAll(em, Field.class).get(0);
        }
        event.setField(field);
        return event;
    }

    @BeforeEach
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();
        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);
        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEvent.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEvent.getEndInscriptionDate()).isEqualTo(DEFAULT_END_INSCRIPTION_DATE);
        assertThat(testEvent.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEvent.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testEvent.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testEvent.getEndInscriptionPlayersDate()).isEqualTo(DEFAULT_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void createEventWithExistingId() throws Exception {
        // Create the Event with an existing ID
        event.setId(1L);
        EventDTO eventDTO = eventMapper.toDto(event);

        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEndInscriptionPlayersDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEndInscriptionPlayersDate(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].endInscriptionDate").value(hasItem(DEFAULT_END_INSCRIPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].endInscriptionPlayersDate").value(hasItem(DEFAULT_END_INSCRIPTION_PLAYERS_DATE.toString())));
    }

    @Test
    @Transactional
    void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc
            .perform(get(ENTITY_API_URL_ID, event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.endInscriptionDate").value(DEFAULT_END_INSCRIPTION_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.endInscriptionPlayersDate").value(DEFAULT_END_INSCRIPTION_PLAYERS_DATE.toString()));
    }

    @Test
    @Transactional
    void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        Long id = event.getId();

        defaultEventShouldBeFound("id.equals=" + id);
        defaultEventShouldNotBeFound("id.notEquals=" + id);

        defaultEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.greaterThan=" + id);

        defaultEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name equals to DEFAULT_NAME
        defaultEventShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventList where name equals to UPDATED_NAME
        defaultEventShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name not equals to DEFAULT_NAME
        defaultEventShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the eventList where name not equals to UPDATED_NAME
        defaultEventShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventList where name equals to UPDATED_NAME
        defaultEventShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name is not null
        defaultEventShouldBeFound("name.specified=true");

        // Get all the eventList where name is null
        defaultEventShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByNameContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name contains DEFAULT_NAME
        defaultEventShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the eventList where name contains UPDATED_NAME
        defaultEventShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name does not contain DEFAULT_NAME
        defaultEventShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the eventList where name does not contain UPDATED_NAME
        defaultEventShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate equals to DEFAULT_FROM_DATE
        defaultEventShouldBeFound("fromDate.equals=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate equals to UPDATED_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByFromDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate not equals to DEFAULT_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.notEquals=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate not equals to UPDATED_FROM_DATE
        defaultEventShouldBeFound("fromDate.notEquals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate in DEFAULT_FROM_DATE or UPDATED_FROM_DATE
        defaultEventShouldBeFound("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE);

        // Get all the eventList where fromDate equals to UPDATED_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is not null
        defaultEventShouldBeFound("fromDate.specified=true");

        // Get all the eventList where fromDate is null
        defaultEventShouldNotBeFound("fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is greater than or equal to DEFAULT_FROM_DATE
        defaultEventShouldBeFound("fromDate.greaterThanOrEqual=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate is greater than or equal to UPDATED_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.greaterThanOrEqual=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByFromDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is less than or equal to DEFAULT_FROM_DATE
        defaultEventShouldBeFound("fromDate.lessThanOrEqual=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate is less than or equal to SMALLER_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.lessThanOrEqual=" + SMALLER_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is less than DEFAULT_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.lessThan=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate is less than UPDATED_FROM_DATE
        defaultEventShouldBeFound("fromDate.lessThan=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByFromDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is greater than DEFAULT_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.greaterThan=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate is greater than SMALLER_FROM_DATE
        defaultEventShouldBeFound("fromDate.greaterThan=" + SMALLER_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate equals to DEFAULT_END_DATE
        defaultEventShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate equals to UPDATED_END_DATE
        defaultEventShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate not equals to DEFAULT_END_DATE
        defaultEventShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate not equals to UPDATED_END_DATE
        defaultEventShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultEventShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the eventList where endDate equals to UPDATED_END_DATE
        defaultEventShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is not null
        defaultEventShouldBeFound("endDate.specified=true");

        // Get all the eventList where endDate is null
        defaultEventShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultEventShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is greater than or equal to UPDATED_END_DATE
        defaultEventShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is less than or equal to DEFAULT_END_DATE
        defaultEventShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is less than or equal to SMALLER_END_DATE
        defaultEventShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is less than DEFAULT_END_DATE
        defaultEventShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is less than UPDATED_END_DATE
        defaultEventShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is greater than DEFAULT_END_DATE
        defaultEventShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is greater than SMALLER_END_DATE
        defaultEventShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate equals to DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.equals=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate equals to UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.equals=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate not equals to DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.notEquals=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate not equals to UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.notEquals=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate in DEFAULT_END_INSCRIPTION_DATE or UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.in=" + DEFAULT_END_INSCRIPTION_DATE + "," + UPDATED_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate equals to UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.in=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is not null
        defaultEventShouldBeFound("endInscriptionDate.specified=true");

        // Get all the eventList where endInscriptionDate is null
        defaultEventShouldNotBeFound("endInscriptionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is greater than or equal to DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.greaterThanOrEqual=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate is greater than or equal to UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.greaterThanOrEqual=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is less than or equal to DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.lessThanOrEqual=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate is less than or equal to SMALLER_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.lessThanOrEqual=" + SMALLER_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is less than DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.lessThan=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate is less than UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.lessThan=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is greater than DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.greaterThan=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate is greater than SMALLER_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.greaterThan=" + SMALLER_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where status equals to DEFAULT_STATUS
        defaultEventShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the eventList where status equals to UPDATED_STATUS
        defaultEventShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where status not equals to DEFAULT_STATUS
        defaultEventShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the eventList where status not equals to UPDATED_STATUS
        defaultEventShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultEventShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the eventList where status equals to UPDATED_STATUS
        defaultEventShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where status is not null
        defaultEventShouldBeFound("status.specified=true");

        // Get all the eventList where status is null
        defaultEventShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createDate equals to DEFAULT_CREATE_DATE
        defaultEventShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the eventList where createDate equals to UPDATED_CREATE_DATE
        defaultEventShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createDate not equals to DEFAULT_CREATE_DATE
        defaultEventShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the eventList where createDate not equals to UPDATED_CREATE_DATE
        defaultEventShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultEventShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the eventList where createDate equals to UPDATED_CREATE_DATE
        defaultEventShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createDate is not null
        defaultEventShouldBeFound("createDate.specified=true");

        // Get all the eventList where createDate is null
        defaultEventShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedDate equals to DEFAULT_UPDATED_DATE
        defaultEventShouldBeFound("updatedDate.equals=" + DEFAULT_UPDATED_DATE);

        // Get all the eventList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultEventShouldNotBeFound("updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByUpdatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedDate not equals to DEFAULT_UPDATED_DATE
        defaultEventShouldNotBeFound("updatedDate.notEquals=" + DEFAULT_UPDATED_DATE);

        // Get all the eventList where updatedDate not equals to UPDATED_UPDATED_DATE
        defaultEventShouldBeFound("updatedDate.notEquals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedDate in DEFAULT_UPDATED_DATE or UPDATED_UPDATED_DATE
        defaultEventShouldBeFound("updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE);

        // Get all the eventList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultEventShouldNotBeFound("updatedDate.in=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedDate is not null
        defaultEventShouldBeFound("updatedDate.specified=true");

        // Get all the eventList where updatedDate is null
        defaultEventShouldNotBeFound("updatedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionPlayersDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionPlayersDate equals to DEFAULT_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldBeFound("endInscriptionPlayersDate.equals=" + DEFAULT_END_INSCRIPTION_PLAYERS_DATE);

        // Get all the eventList where endInscriptionPlayersDate equals to UPDATED_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldNotBeFound("endInscriptionPlayersDate.equals=" + UPDATED_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionPlayersDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionPlayersDate not equals to DEFAULT_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldNotBeFound("endInscriptionPlayersDate.notEquals=" + DEFAULT_END_INSCRIPTION_PLAYERS_DATE);

        // Get all the eventList where endInscriptionPlayersDate not equals to UPDATED_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldBeFound("endInscriptionPlayersDate.notEquals=" + UPDATED_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionPlayersDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionPlayersDate in DEFAULT_END_INSCRIPTION_PLAYERS_DATE or UPDATED_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldBeFound(
            "endInscriptionPlayersDate.in=" + DEFAULT_END_INSCRIPTION_PLAYERS_DATE + "," + UPDATED_END_INSCRIPTION_PLAYERS_DATE
        );

        // Get all the eventList where endInscriptionPlayersDate equals to UPDATED_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldNotBeFound("endInscriptionPlayersDate.in=" + UPDATED_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionPlayersDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionPlayersDate is not null
        defaultEventShouldBeFound("endInscriptionPlayersDate.specified=true");

        // Get all the eventList where endInscriptionPlayersDate is null
        defaultEventShouldNotBeFound("endInscriptionPlayersDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionPlayersDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionPlayersDate is greater than or equal to DEFAULT_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldBeFound("endInscriptionPlayersDate.greaterThanOrEqual=" + DEFAULT_END_INSCRIPTION_PLAYERS_DATE);

        // Get all the eventList where endInscriptionPlayersDate is greater than or equal to UPDATED_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldNotBeFound("endInscriptionPlayersDate.greaterThanOrEqual=" + UPDATED_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionPlayersDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionPlayersDate is less than or equal to DEFAULT_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldBeFound("endInscriptionPlayersDate.lessThanOrEqual=" + DEFAULT_END_INSCRIPTION_PLAYERS_DATE);

        // Get all the eventList where endInscriptionPlayersDate is less than or equal to SMALLER_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldNotBeFound("endInscriptionPlayersDate.lessThanOrEqual=" + SMALLER_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionPlayersDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionPlayersDate is less than DEFAULT_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldNotBeFound("endInscriptionPlayersDate.lessThan=" + DEFAULT_END_INSCRIPTION_PLAYERS_DATE);

        // Get all the eventList where endInscriptionPlayersDate is less than UPDATED_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldBeFound("endInscriptionPlayersDate.lessThan=" + UPDATED_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEndInscriptionPlayersDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionPlayersDate is greater than DEFAULT_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldNotBeFound("endInscriptionPlayersDate.greaterThan=" + DEFAULT_END_INSCRIPTION_PLAYERS_DATE);

        // Get all the eventList where endInscriptionPlayersDate is greater than SMALLER_END_INSCRIPTION_PLAYERS_DATE
        defaultEventShouldBeFound("endInscriptionPlayersDate.greaterThan=" + SMALLER_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        event.setTournament(tournament);
        eventRepository.saveAndFlush(event);
        Long tournamentId = tournament.getId();

        // Get all the eventList where tournament equals to tournamentId
        defaultEventShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the eventList where tournament equals to (tournamentId + 1)
        defaultEventShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    @Test
    @Transactional
    void getAllEventsByFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        Field field = FieldResourceIT.createEntity(em);
        em.persist(field);
        em.flush();
        event.setField(field);
        eventRepository.saveAndFlush(event);
        Long fieldId = field.getId();

        // Get all the eventList where field equals to fieldId
        defaultEventShouldBeFound("fieldId.equals=" + fieldId);

        // Get all the eventList where field equals to (fieldId + 1)
        defaultEventShouldNotBeFound("fieldId.equals=" + (fieldId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].endInscriptionDate").value(hasItem(DEFAULT_END_INSCRIPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].endInscriptionPlayersDate").value(hasItem(DEFAULT_END_INSCRIPTION_PLAYERS_DATE.toString())));

        // Check, that the count call also returns 1
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .endDate(UPDATED_END_DATE)
            .endInscriptionDate(UPDATED_END_INSCRIPTION_DATE)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .endInscriptionPlayersDate(UPDATED_END_INSCRIPTION_PLAYERS_DATE);
        EventDTO eventDTO = eventMapper.toDto(updatedEvent);

        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEvent.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getEndInscriptionDate()).isEqualTo(UPDATED_END_INSCRIPTION_DATE);
        assertThat(testEvent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEvent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testEvent.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testEvent.getEndInscriptionPlayersDate()).isEqualTo(UPDATED_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void putNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .name(UPDATED_NAME)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .endInscriptionPlayersDate(UPDATED_END_INSCRIPTION_PLAYERS_DATE);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEvent.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getEndInscriptionDate()).isEqualTo(DEFAULT_END_INSCRIPTION_DATE);
        assertThat(testEvent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEvent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testEvent.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testEvent.getEndInscriptionPlayersDate()).isEqualTo(UPDATED_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void fullUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .endDate(UPDATED_END_DATE)
            .endInscriptionDate(UPDATED_END_INSCRIPTION_DATE)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .endInscriptionPlayersDate(UPDATED_END_INSCRIPTION_PLAYERS_DATE);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEvent.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getEndInscriptionDate()).isEqualTo(UPDATED_END_INSCRIPTION_DATE);
        assertThat(testEvent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEvent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testEvent.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testEvent.getEndInscriptionPlayersDate()).isEqualTo(UPDATED_END_INSCRIPTION_PLAYERS_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Delete the event
        restEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, event.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
