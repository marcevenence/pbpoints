package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.Equipment;
import com.pbpoints.domain.User;
import com.pbpoints.repository.EquipmentRepository;
import com.pbpoints.service.criteria.EquipmentCriteria;
import com.pbpoints.service.dto.EquipmentDTO;
import com.pbpoints.service.mapper.EquipmentMapper;
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
 * Integration tests for the {@link EquipmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EquipmentResourceIT {

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE_1 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE_1 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_1_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_1_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_PICTURE_2 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE_2 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_2_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_2_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/equipment";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createEntity(EntityManager em) {
        Equipment equipment = new Equipment()
            .brand(DEFAULT_BRAND)
            .model(DEFAULT_MODEL)
            .picture1(DEFAULT_PICTURE_1)
            .picture1ContentType(DEFAULT_PICTURE_1_CONTENT_TYPE)
            .picture2(DEFAULT_PICTURE_2)
            .picture2ContentType(DEFAULT_PICTURE_2_CONTENT_TYPE)
            .serial(DEFAULT_SERIAL);
        return equipment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createUpdatedEntity(EntityManager em) {
        Equipment equipment = new Equipment()
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .picture1(UPDATED_PICTURE_1)
            .picture1ContentType(UPDATED_PICTURE_1_CONTENT_TYPE)
            .picture2(UPDATED_PICTURE_2)
            .picture2ContentType(UPDATED_PICTURE_2_CONTENT_TYPE)
            .serial(UPDATED_SERIAL);
        return equipment;
    }

    @BeforeEach
    public void initTest() {
        equipment = createEntity(em);
    }

    @Test
    @Transactional
    void createEquipment() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();
        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);
        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate + 1);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testEquipment.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testEquipment.getPicture1()).isEqualTo(DEFAULT_PICTURE_1);
        assertThat(testEquipment.getPicture1ContentType()).isEqualTo(DEFAULT_PICTURE_1_CONTENT_TYPE);
        assertThat(testEquipment.getPicture2()).isEqualTo(DEFAULT_PICTURE_2);
        assertThat(testEquipment.getPicture2ContentType()).isEqualTo(DEFAULT_PICTURE_2_CONTENT_TYPE);
        assertThat(testEquipment.getSerial()).isEqualTo(DEFAULT_SERIAL);
    }

    @Test
    @Transactional
    void createEquipmentWithExistingId() throws Exception {
        // Create the Equipment with an existing ID
        equipment.setId(1L);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setBrand(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setModel(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].picture1ContentType").value(hasItem(DEFAULT_PICTURE_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture1").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE_1))))
            .andExpect(jsonPath("$.[*].picture2ContentType").value(hasItem(DEFAULT_PICTURE_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture2").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE_2))))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)));
    }

    @Test
    @Transactional
    void getEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get the equipment
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipment.getId().intValue()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.picture1ContentType").value(DEFAULT_PICTURE_1_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture1").value(Base64Utils.encodeToString(DEFAULT_PICTURE_1)))
            .andExpect(jsonPath("$.picture2ContentType").value(DEFAULT_PICTURE_2_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture2").value(Base64Utils.encodeToString(DEFAULT_PICTURE_2)))
            .andExpect(jsonPath("$.serial").value(DEFAULT_SERIAL));
    }

    @Test
    @Transactional
    void getEquipmentByIdFiltering() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        Long id = equipment.getId();

        defaultEquipmentShouldBeFound("id.equals=" + id);
        defaultEquipmentShouldNotBeFound("id.notEquals=" + id);

        defaultEquipmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipmentShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand equals to DEFAULT_BRAND
        defaultEquipmentShouldBeFound("brand.equals=" + DEFAULT_BRAND);

        // Get all the equipmentList where brand equals to UPDATED_BRAND
        defaultEquipmentShouldNotBeFound("brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand not equals to DEFAULT_BRAND
        defaultEquipmentShouldNotBeFound("brand.notEquals=" + DEFAULT_BRAND);

        // Get all the equipmentList where brand not equals to UPDATED_BRAND
        defaultEquipmentShouldBeFound("brand.notEquals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand in DEFAULT_BRAND or UPDATED_BRAND
        defaultEquipmentShouldBeFound("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND);

        // Get all the equipmentList where brand equals to UPDATED_BRAND
        defaultEquipmentShouldNotBeFound("brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand is not null
        defaultEquipmentShouldBeFound("brand.specified=true");

        // Get all the equipmentList where brand is null
        defaultEquipmentShouldNotBeFound("brand.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand contains DEFAULT_BRAND
        defaultEquipmentShouldBeFound("brand.contains=" + DEFAULT_BRAND);

        // Get all the equipmentList where brand contains UPDATED_BRAND
        defaultEquipmentShouldNotBeFound("brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand does not contain DEFAULT_BRAND
        defaultEquipmentShouldNotBeFound("brand.doesNotContain=" + DEFAULT_BRAND);

        // Get all the equipmentList where brand does not contain UPDATED_BRAND
        defaultEquipmentShouldBeFound("brand.doesNotContain=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllEquipmentByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model equals to DEFAULT_MODEL
        defaultEquipmentShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the equipmentList where model equals to UPDATED_MODEL
        defaultEquipmentShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllEquipmentByModelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model not equals to DEFAULT_MODEL
        defaultEquipmentShouldNotBeFound("model.notEquals=" + DEFAULT_MODEL);

        // Get all the equipmentList where model not equals to UPDATED_MODEL
        defaultEquipmentShouldBeFound("model.notEquals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllEquipmentByModelIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultEquipmentShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the equipmentList where model equals to UPDATED_MODEL
        defaultEquipmentShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllEquipmentByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model is not null
        defaultEquipmentShouldBeFound("model.specified=true");

        // Get all the equipmentList where model is null
        defaultEquipmentShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentByModelContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model contains DEFAULT_MODEL
        defaultEquipmentShouldBeFound("model.contains=" + DEFAULT_MODEL);

        // Get all the equipmentList where model contains UPDATED_MODEL
        defaultEquipmentShouldNotBeFound("model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllEquipmentByModelNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model does not contain DEFAULT_MODEL
        defaultEquipmentShouldNotBeFound("model.doesNotContain=" + DEFAULT_MODEL);

        // Get all the equipmentList where model does not contain UPDATED_MODEL
        defaultEquipmentShouldBeFound("model.doesNotContain=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllEquipmentBySerialIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where serial equals to DEFAULT_SERIAL
        defaultEquipmentShouldBeFound("serial.equals=" + DEFAULT_SERIAL);

        // Get all the equipmentList where serial equals to UPDATED_SERIAL
        defaultEquipmentShouldNotBeFound("serial.equals=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllEquipmentBySerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where serial not equals to DEFAULT_SERIAL
        defaultEquipmentShouldNotBeFound("serial.notEquals=" + DEFAULT_SERIAL);

        // Get all the equipmentList where serial not equals to UPDATED_SERIAL
        defaultEquipmentShouldBeFound("serial.notEquals=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllEquipmentBySerialIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where serial in DEFAULT_SERIAL or UPDATED_SERIAL
        defaultEquipmentShouldBeFound("serial.in=" + DEFAULT_SERIAL + "," + UPDATED_SERIAL);

        // Get all the equipmentList where serial equals to UPDATED_SERIAL
        defaultEquipmentShouldNotBeFound("serial.in=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllEquipmentBySerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where serial is not null
        defaultEquipmentShouldBeFound("serial.specified=true");

        // Get all the equipmentList where serial is null
        defaultEquipmentShouldNotBeFound("serial.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentBySerialContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where serial contains DEFAULT_SERIAL
        defaultEquipmentShouldBeFound("serial.contains=" + DEFAULT_SERIAL);

        // Get all the equipmentList where serial contains UPDATED_SERIAL
        defaultEquipmentShouldNotBeFound("serial.contains=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllEquipmentBySerialNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where serial does not contain DEFAULT_SERIAL
        defaultEquipmentShouldNotBeFound("serial.doesNotContain=" + DEFAULT_SERIAL);

        // Get all the equipmentList where serial does not contain UPDATED_SERIAL
        defaultEquipmentShouldBeFound("serial.doesNotContain=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllEquipmentByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        equipment.setUser(user);
        equipmentRepository.saveAndFlush(equipment);
        Long userId = user.getId();

        // Get all the equipmentList where user equals to userId
        defaultEquipmentShouldBeFound("userId.equals=" + userId);

        // Get all the equipmentList where user equals to (userId + 1)
        defaultEquipmentShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipmentShouldBeFound(String filter) throws Exception {
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].picture1ContentType").value(hasItem(DEFAULT_PICTURE_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture1").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE_1))))
            .andExpect(jsonPath("$.[*].picture2ContentType").value(hasItem(DEFAULT_PICTURE_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture2").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE_2))))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)));

        // Check, that the count call also returns 1
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipmentShouldNotBeFound(String filter) throws Exception {
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment
        Equipment updatedEquipment = equipmentRepository.findById(equipment.getId()).get();
        // Disconnect from session so that the updates on updatedEquipment are not directly saved in db
        em.detach(updatedEquipment);
        updatedEquipment
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .picture1(UPDATED_PICTURE_1)
            .picture1ContentType(UPDATED_PICTURE_1_CONTENT_TYPE)
            .picture2(UPDATED_PICTURE_2)
            .picture2ContentType(UPDATED_PICTURE_2_CONTENT_TYPE)
            .serial(UPDATED_SERIAL);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(updatedEquipment);

        restEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testEquipment.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testEquipment.getPicture1()).isEqualTo(UPDATED_PICTURE_1);
        assertThat(testEquipment.getPicture1ContentType()).isEqualTo(UPDATED_PICTURE_1_CONTENT_TYPE);
        assertThat(testEquipment.getPicture2()).isEqualTo(UPDATED_PICTURE_2);
        assertThat(testEquipment.getPicture2ContentType()).isEqualTo(UPDATED_PICTURE_2_CONTENT_TYPE);
        assertThat(testEquipment.getSerial()).isEqualTo(UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void putNonExistingEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipmentWithPatch() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment using partial update
        Equipment partialUpdatedEquipment = new Equipment();
        partialUpdatedEquipment.setId(equipment.getId());

        partialUpdatedEquipment
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .picture1(UPDATED_PICTURE_1)
            .picture1ContentType(UPDATED_PICTURE_1_CONTENT_TYPE)
            .serial(UPDATED_SERIAL);

        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipment))
            )
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testEquipment.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testEquipment.getPicture1()).isEqualTo(UPDATED_PICTURE_1);
        assertThat(testEquipment.getPicture1ContentType()).isEqualTo(UPDATED_PICTURE_1_CONTENT_TYPE);
        assertThat(testEquipment.getPicture2()).isEqualTo(DEFAULT_PICTURE_2);
        assertThat(testEquipment.getPicture2ContentType()).isEqualTo(DEFAULT_PICTURE_2_CONTENT_TYPE);
        assertThat(testEquipment.getSerial()).isEqualTo(UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void fullUpdateEquipmentWithPatch() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment using partial update
        Equipment partialUpdatedEquipment = new Equipment();
        partialUpdatedEquipment.setId(equipment.getId());

        partialUpdatedEquipment
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .picture1(UPDATED_PICTURE_1)
            .picture1ContentType(UPDATED_PICTURE_1_CONTENT_TYPE)
            .picture2(UPDATED_PICTURE_2)
            .picture2ContentType(UPDATED_PICTURE_2_CONTENT_TYPE)
            .serial(UPDATED_SERIAL);

        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipment))
            )
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testEquipment.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testEquipment.getPicture1()).isEqualTo(UPDATED_PICTURE_1);
        assertThat(testEquipment.getPicture1ContentType()).isEqualTo(UPDATED_PICTURE_1_CONTENT_TYPE);
        assertThat(testEquipment.getPicture2()).isEqualTo(UPDATED_PICTURE_2);
        assertThat(testEquipment.getPicture2ContentType()).isEqualTo(UPDATED_PICTURE_2_CONTENT_TYPE);
        assertThat(testEquipment.getSerial()).isEqualTo(UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void patchNonExistingEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeDelete = equipmentRepository.findAll().size();

        // Delete the equipment
        restEquipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
