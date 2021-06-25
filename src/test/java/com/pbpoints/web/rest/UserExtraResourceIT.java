package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.DocType;
import com.pbpoints.domain.User;
import com.pbpoints.domain.UserExtra;
import com.pbpoints.repository.UserExtraRepository;
import com.pbpoints.service.criteria.UserExtraCriteria;
import com.pbpoints.service.dto.UserExtraDTO;
import com.pbpoints.service.mapper.UserExtraMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link UserExtraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserExtraResourceIT {

    private static final String DEFAULT_NUM_DOC = "AAAAAAAAAA";
    private static final String UPDATED_NUM_DOC = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BORN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BORN_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BORN_DATE = LocalDate.ofEpochDay(-1L);

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/user-extras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserExtraRepository userExtraRepository;

    @Autowired
    private UserExtraMapper userExtraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserExtraMockMvc;

    private UserExtra userExtra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra()
            .numDoc(DEFAULT_NUM_DOC)
            .phone(DEFAULT_PHONE)
            .bornDate(DEFAULT_BORN_DATE)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userExtra.setUser(user);
        return userExtra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createUpdatedEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra()
            .numDoc(UPDATED_NUM_DOC)
            .phone(UPDATED_PHONE)
            .bornDate(UPDATED_BORN_DATE)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userExtra.setUser(user);
        return userExtra;
    }

    @BeforeEach
    public void initTest() {
        userExtra = createEntity(em);
    }

    @Test
    @Transactional
    void createUserExtra() throws Exception {
        int databaseSizeBeforeCreate = userExtraRepository.findAll().size();
        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);
        restUserExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtraDTO)))
            .andExpect(status().isCreated());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeCreate + 1);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getNumDoc()).isEqualTo(DEFAULT_NUM_DOC);
        assertThat(testUserExtra.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserExtra.getBornDate()).isEqualTo(DEFAULT_BORN_DATE);
        assertThat(testUserExtra.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testUserExtra.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createUserExtraWithExistingId() throws Exception {
        // Create the UserExtra with an existing ID
        userExtra.setId(1L);
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        int databaseSizeBeforeCreate = userExtraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserExtras() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].numDoc").value(hasItem(DEFAULT_NUM_DOC)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].bornDate").value(hasItem(DEFAULT_BORN_DATE.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }

    @Test
    @Transactional
    void getUserExtra() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get the userExtra
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL_ID, userExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userExtra.getId().intValue()))
            .andExpect(jsonPath("$.numDoc").value(DEFAULT_NUM_DOC))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.bornDate").value(DEFAULT_BORN_DATE.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    void getUserExtrasByIdFiltering() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        Long id = userExtra.getId();

        defaultUserExtraShouldBeFound("id.equals=" + id);
        defaultUserExtraShouldNotBeFound("id.notEquals=" + id);

        defaultUserExtraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserExtraShouldNotBeFound("id.greaterThan=" + id);

        defaultUserExtraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserExtraShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserExtrasByNumDocIsEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where numDoc equals to DEFAULT_NUM_DOC
        defaultUserExtraShouldBeFound("numDoc.equals=" + DEFAULT_NUM_DOC);

        // Get all the userExtraList where numDoc equals to UPDATED_NUM_DOC
        defaultUserExtraShouldNotBeFound("numDoc.equals=" + UPDATED_NUM_DOC);
    }

    @Test
    @Transactional
    void getAllUserExtrasByNumDocIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where numDoc not equals to DEFAULT_NUM_DOC
        defaultUserExtraShouldNotBeFound("numDoc.notEquals=" + DEFAULT_NUM_DOC);

        // Get all the userExtraList where numDoc not equals to UPDATED_NUM_DOC
        defaultUserExtraShouldBeFound("numDoc.notEquals=" + UPDATED_NUM_DOC);
    }

    @Test
    @Transactional
    void getAllUserExtrasByNumDocIsInShouldWork() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where numDoc in DEFAULT_NUM_DOC or UPDATED_NUM_DOC
        defaultUserExtraShouldBeFound("numDoc.in=" + DEFAULT_NUM_DOC + "," + UPDATED_NUM_DOC);

        // Get all the userExtraList where numDoc equals to UPDATED_NUM_DOC
        defaultUserExtraShouldNotBeFound("numDoc.in=" + UPDATED_NUM_DOC);
    }

    @Test
    @Transactional
    void getAllUserExtrasByNumDocIsNullOrNotNull() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where numDoc is not null
        defaultUserExtraShouldBeFound("numDoc.specified=true");

        // Get all the userExtraList where numDoc is null
        defaultUserExtraShouldNotBeFound("numDoc.specified=false");
    }

    @Test
    @Transactional
    void getAllUserExtrasByNumDocContainsSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where numDoc contains DEFAULT_NUM_DOC
        defaultUserExtraShouldBeFound("numDoc.contains=" + DEFAULT_NUM_DOC);

        // Get all the userExtraList where numDoc contains UPDATED_NUM_DOC
        defaultUserExtraShouldNotBeFound("numDoc.contains=" + UPDATED_NUM_DOC);
    }

    @Test
    @Transactional
    void getAllUserExtrasByNumDocNotContainsSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where numDoc does not contain DEFAULT_NUM_DOC
        defaultUserExtraShouldNotBeFound("numDoc.doesNotContain=" + DEFAULT_NUM_DOC);

        // Get all the userExtraList where numDoc does not contain UPDATED_NUM_DOC
        defaultUserExtraShouldBeFound("numDoc.doesNotContain=" + UPDATED_NUM_DOC);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone equals to DEFAULT_PHONE
        defaultUserExtraShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the userExtraList where phone equals to UPDATED_PHONE
        defaultUserExtraShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone not equals to DEFAULT_PHONE
        defaultUserExtraShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the userExtraList where phone not equals to UPDATED_PHONE
        defaultUserExtraShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultUserExtraShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the userExtraList where phone equals to UPDATED_PHONE
        defaultUserExtraShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone is not null
        defaultUserExtraShouldBeFound("phone.specified=true");

        // Get all the userExtraList where phone is null
        defaultUserExtraShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneContainsSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone contains DEFAULT_PHONE
        defaultUserExtraShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the userExtraList where phone contains UPDATED_PHONE
        defaultUserExtraShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone does not contain DEFAULT_PHONE
        defaultUserExtraShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the userExtraList where phone does not contain UPDATED_PHONE
        defaultUserExtraShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByBornDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where bornDate equals to DEFAULT_BORN_DATE
        defaultUserExtraShouldBeFound("bornDate.equals=" + DEFAULT_BORN_DATE);

        // Get all the userExtraList where bornDate equals to UPDATED_BORN_DATE
        defaultUserExtraShouldNotBeFound("bornDate.equals=" + UPDATED_BORN_DATE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByBornDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where bornDate not equals to DEFAULT_BORN_DATE
        defaultUserExtraShouldNotBeFound("bornDate.notEquals=" + DEFAULT_BORN_DATE);

        // Get all the userExtraList where bornDate not equals to UPDATED_BORN_DATE
        defaultUserExtraShouldBeFound("bornDate.notEquals=" + UPDATED_BORN_DATE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByBornDateIsInShouldWork() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where bornDate in DEFAULT_BORN_DATE or UPDATED_BORN_DATE
        defaultUserExtraShouldBeFound("bornDate.in=" + DEFAULT_BORN_DATE + "," + UPDATED_BORN_DATE);

        // Get all the userExtraList where bornDate equals to UPDATED_BORN_DATE
        defaultUserExtraShouldNotBeFound("bornDate.in=" + UPDATED_BORN_DATE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByBornDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where bornDate is not null
        defaultUserExtraShouldBeFound("bornDate.specified=true");

        // Get all the userExtraList where bornDate is null
        defaultUserExtraShouldNotBeFound("bornDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUserExtrasByBornDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where bornDate is greater than or equal to DEFAULT_BORN_DATE
        defaultUserExtraShouldBeFound("bornDate.greaterThanOrEqual=" + DEFAULT_BORN_DATE);

        // Get all the userExtraList where bornDate is greater than or equal to UPDATED_BORN_DATE
        defaultUserExtraShouldNotBeFound("bornDate.greaterThanOrEqual=" + UPDATED_BORN_DATE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByBornDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where bornDate is less than or equal to DEFAULT_BORN_DATE
        defaultUserExtraShouldBeFound("bornDate.lessThanOrEqual=" + DEFAULT_BORN_DATE);

        // Get all the userExtraList where bornDate is less than or equal to SMALLER_BORN_DATE
        defaultUserExtraShouldNotBeFound("bornDate.lessThanOrEqual=" + SMALLER_BORN_DATE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByBornDateIsLessThanSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where bornDate is less than DEFAULT_BORN_DATE
        defaultUserExtraShouldNotBeFound("bornDate.lessThan=" + DEFAULT_BORN_DATE);

        // Get all the userExtraList where bornDate is less than UPDATED_BORN_DATE
        defaultUserExtraShouldBeFound("bornDate.lessThan=" + UPDATED_BORN_DATE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByBornDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where bornDate is greater than DEFAULT_BORN_DATE
        defaultUserExtraShouldNotBeFound("bornDate.greaterThan=" + DEFAULT_BORN_DATE);

        // Get all the userExtraList where bornDate is greater than SMALLER_BORN_DATE
        defaultUserExtraShouldBeFound("bornDate.greaterThan=" + SMALLER_BORN_DATE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = userExtra.getUser();
        userExtraRepository.saveAndFlush(userExtra);
        Long userId = user.getId();

        // Get all the userExtraList where user equals to userId
        defaultUserExtraShouldBeFound("userId.equals=" + userId);

        // Get all the userExtraList where user equals to (userId + 1)
        defaultUserExtraShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUserExtrasByDocTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);
        DocType docType = DocTypeResourceIT.createEntity(em);
        em.persist(docType);
        em.flush();
        userExtra.setDocType(docType);
        userExtraRepository.saveAndFlush(userExtra);
        Long docTypeId = docType.getId();

        // Get all the userExtraList where docType equals to docTypeId
        defaultUserExtraShouldBeFound("docTypeId.equals=" + docTypeId);

        // Get all the userExtraList where docType equals to (docTypeId + 1)
        defaultUserExtraShouldNotBeFound("docTypeId.equals=" + (docTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserExtraShouldBeFound(String filter) throws Exception {
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].numDoc").value(hasItem(DEFAULT_NUM_DOC)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].bornDate").value(hasItem(DEFAULT_BORN_DATE.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));

        // Check, that the count call also returns 1
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserExtraShouldNotBeFound(String filter) throws Exception {
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserExtra() throws Exception {
        // Get the userExtra
        restUserExtraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserExtra() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();

        // Update the userExtra
        UserExtra updatedUserExtra = userExtraRepository.findById(userExtra.getId()).get();
        // Disconnect from session so that the updates on updatedUserExtra are not directly saved in db
        em.detach(updatedUserExtra);
        updatedUserExtra
            .numDoc(UPDATED_NUM_DOC)
            .phone(UPDATED_PHONE)
            .bornDate(UPDATED_BORN_DATE)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(updatedUserExtra);

        restUserExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userExtraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getNumDoc()).isEqualTo(UPDATED_NUM_DOC);
        assertThat(testUserExtra.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserExtra.getBornDate()).isEqualTo(UPDATED_BORN_DATE);
        assertThat(testUserExtra.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testUserExtra.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingUserExtra() throws Exception {
        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();
        userExtra.setId(count.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userExtraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserExtra() throws Exception {
        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();
        userExtra.setId(count.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserExtra() throws Exception {
        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();
        userExtra.setId(count.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserExtraWithPatch() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();

        // Update the userExtra using partial update
        UserExtra partialUpdatedUserExtra = new UserExtra();
        partialUpdatedUserExtra.setId(userExtra.getId());

        partialUpdatedUserExtra.phone(UPDATED_PHONE).picture(UPDATED_PICTURE).pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserExtra))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getNumDoc()).isEqualTo(DEFAULT_NUM_DOC);
        assertThat(testUserExtra.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserExtra.getBornDate()).isEqualTo(DEFAULT_BORN_DATE);
        assertThat(testUserExtra.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testUserExtra.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateUserExtraWithPatch() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();

        // Update the userExtra using partial update
        UserExtra partialUpdatedUserExtra = new UserExtra();
        partialUpdatedUserExtra.setId(userExtra.getId());

        partialUpdatedUserExtra
            .numDoc(UPDATED_NUM_DOC)
            .phone(UPDATED_PHONE)
            .bornDate(UPDATED_BORN_DATE)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserExtra))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getNumDoc()).isEqualTo(UPDATED_NUM_DOC);
        assertThat(testUserExtra.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserExtra.getBornDate()).isEqualTo(UPDATED_BORN_DATE);
        assertThat(testUserExtra.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testUserExtra.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingUserExtra() throws Exception {
        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();
        userExtra.setId(count.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userExtraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserExtra() throws Exception {
        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();
        userExtra.setId(count.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserExtra() throws Exception {
        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();
        userExtra.setId(count.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserExtra() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        int databaseSizeBeforeDelete = userExtraRepository.findAll().size();

        // Delete the userExtra
        restUserExtraMockMvc
            .perform(delete(ENTITY_API_URL_ID, userExtra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
