package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.City;
import com.pbpoints.domain.Province;
import com.pbpoints.repository.CityRepository;
import com.pbpoints.service.criteria.CityCriteria;
import com.pbpoints.service.dto.CityDTO;
import com.pbpoints.service.mapper.CityMapper;
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
 * Integration tests for the {@link CityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCityMockMvc;

    private City city;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createEntity(EntityManager em) {
        City city = new City().name(DEFAULT_NAME).latitude(DEFAULT_LATITUDE).longitude(DEFAULT_LONGITUDE);
        return city;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createUpdatedEntity(EntityManager em) {
        City city = new City().name(UPDATED_NAME).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);
        return city;
    }

    @BeforeEach
    public void initTest() {
        city = createEntity(em);
    }

    @Test
    @Transactional
    void createCity() throws Exception {
        int databaseSizeBeforeCreate = cityRepository.findAll().size();
        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);
        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isCreated());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate + 1);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCity.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testCity.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void createCityWithExistingId() throws Exception {
        // Create the City with an existing ID
        city.setId(1L);
        CityDTO cityDTO = cityMapper.toDto(city);

        int databaseSizeBeforeCreate = cityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCities() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)));
    }

    @Test
    @Transactional
    void getCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get the city
        restCityMockMvc
            .perform(get(ENTITY_API_URL_ID, city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(city.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE));
    }

    @Test
    @Transactional
    void getCitiesByIdFiltering() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        Long id = city.getId();

        defaultCityShouldBeFound("id.equals=" + id);
        defaultCityShouldNotBeFound("id.notEquals=" + id);

        defaultCityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCityShouldNotBeFound("id.greaterThan=" + id);

        defaultCityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where name equals to DEFAULT_NAME
        defaultCityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cityList where name equals to UPDATED_NAME
        defaultCityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where name not equals to DEFAULT_NAME
        defaultCityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the cityList where name not equals to UPDATED_NAME
        defaultCityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cityList where name equals to UPDATED_NAME
        defaultCityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where name is not null
        defaultCityShouldBeFound("name.specified=true");

        // Get all the cityList where name is null
        defaultCityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where name contains DEFAULT_NAME
        defaultCityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the cityList where name contains UPDATED_NAME
        defaultCityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where name does not contain DEFAULT_NAME
        defaultCityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the cityList where name does not contain UPDATED_NAME
        defaultCityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where latitude equals to DEFAULT_LATITUDE
        defaultCityShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the cityList where latitude equals to UPDATED_LATITUDE
        defaultCityShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where latitude not equals to DEFAULT_LATITUDE
        defaultCityShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the cityList where latitude not equals to UPDATED_LATITUDE
        defaultCityShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultCityShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the cityList where latitude equals to UPDATED_LATITUDE
        defaultCityShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where latitude is not null
        defaultCityShouldBeFound("latitude.specified=true");

        // Get all the cityList where latitude is null
        defaultCityShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where latitude contains DEFAULT_LATITUDE
        defaultCityShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the cityList where latitude contains UPDATED_LATITUDE
        defaultCityShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where latitude does not contain DEFAULT_LATITUDE
        defaultCityShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the cityList where latitude does not contain UPDATED_LATITUDE
        defaultCityShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where longitude equals to DEFAULT_LONGITUDE
        defaultCityShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the cityList where longitude equals to UPDATED_LONGITUDE
        defaultCityShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where longitude not equals to DEFAULT_LONGITUDE
        defaultCityShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the cityList where longitude not equals to UPDATED_LONGITUDE
        defaultCityShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultCityShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the cityList where longitude equals to UPDATED_LONGITUDE
        defaultCityShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where longitude is not null
        defaultCityShouldBeFound("longitude.specified=true");

        // Get all the cityList where longitude is null
        defaultCityShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where longitude contains DEFAULT_LONGITUDE
        defaultCityShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the cityList where longitude contains UPDATED_LONGITUDE
        defaultCityShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where longitude does not contain DEFAULT_LONGITUDE
        defaultCityShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the cityList where longitude does not contain UPDATED_LONGITUDE
        defaultCityShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCitiesByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);
        Province province = ProvinceResourceIT.createEntity(em);
        em.persist(province);
        em.flush();
        city.setProvince(province);
        cityRepository.saveAndFlush(city);
        Long provinceId = province.getId();

        // Get all the cityList where province equals to provinceId
        defaultCityShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the cityList where province equals to (provinceId + 1)
        defaultCityShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCityShouldBeFound(String filter) throws Exception {
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)));

        // Check, that the count call also returns 1
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCityShouldNotBeFound(String filter) throws Exception {
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCity() throws Exception {
        // Get the city
        restCityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city
        City updatedCity = cityRepository.findById(city.getId()).get();
        // Disconnect from session so that the updates on updatedCity are not directly saved in db
        em.detach(updatedCity);
        updatedCity.name(UPDATED_NAME).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);
        CityDTO cityDTO = cityMapper.toDto(updatedCity);

        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCity.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testCity.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void putNonExistingCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCityWithPatch() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCity.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testCity.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void fullUpdateCityWithPatch() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        partialUpdatedCity.name(UPDATED_NAME).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCity.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testCity.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeDelete = cityRepository.findAll().size();

        // Delete the city
        restCityMockMvc
            .perform(delete(ENTITY_API_URL_ID, city.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
