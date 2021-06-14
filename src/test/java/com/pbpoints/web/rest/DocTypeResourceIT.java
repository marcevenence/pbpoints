package com.pbpoints.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pbpoints.IntegrationTest;
import com.pbpoints.domain.DocType;
import com.pbpoints.repository.DocTypeRepository;
import com.pbpoints.service.dto.DocTypeDTO;
import com.pbpoints.service.mapper.DocTypeMapper;
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
 * Integration tests for the {@link DocTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocTypeRepository docTypeRepository;

    @Autowired
    private DocTypeMapper docTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocTypeMockMvc;

    private DocType docType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocType createEntity(EntityManager em) {
        DocType docType = new DocType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return docType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocType createUpdatedEntity(EntityManager em) {
        DocType docType = new DocType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return docType;
    }

    @BeforeEach
    public void initTest() {
        docType = createEntity(em);
    }

    @Test
    @Transactional
    void createDocType() throws Exception {
        int databaseSizeBeforeCreate = docTypeRepository.findAll().size();
        // Create the DocType
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);
        restDocTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DocType testDocType = docTypeList.get(docTypeList.size() - 1);
        assertThat(testDocType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDocTypeWithExistingId() throws Exception {
        // Create the DocType with an existing ID
        docType.setId(1L);
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);

        int databaseSizeBeforeCreate = docTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocTypes() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        // Get all the docTypeList
        restDocTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDocType() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        // Get the docType
        restDocTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, docType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDocType() throws Exception {
        // Get the docType
        restDocTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocType() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();

        // Update the docType
        DocType updatedDocType = docTypeRepository.findById(docType.getId()).get();
        // Disconnect from session so that the updates on updatedDocType are not directly saved in db
        em.detach(updatedDocType);
        updatedDocType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(updatedDocType);

        restDocTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
        DocType testDocType = docTypeList.get(docTypeList.size() - 1);
        assertThat(testDocType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingDocType() throws Exception {
        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();
        docType.setId(count.incrementAndGet());

        // Create the DocType
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocType() throws Exception {
        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();
        docType.setId(count.incrementAndGet());

        // Create the DocType
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocType() throws Exception {
        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();
        docType.setId(count.incrementAndGet());

        // Create the DocType
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocTypeWithPatch() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();

        // Update the docType using partial update
        DocType partialUpdatedDocType = new DocType();
        partialUpdatedDocType.setId(docType.getId());

        partialUpdatedDocType.name(UPDATED_NAME);

        restDocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocType))
            )
            .andExpect(status().isOk());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
        DocType testDocType = docTypeList.get(docTypeList.size() - 1);
        assertThat(testDocType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDocTypeWithPatch() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();

        // Update the docType using partial update
        DocType partialUpdatedDocType = new DocType();
        partialUpdatedDocType.setId(docType.getId());

        partialUpdatedDocType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restDocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocType))
            )
            .andExpect(status().isOk());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
        DocType testDocType = docTypeList.get(docTypeList.size() - 1);
        assertThat(testDocType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDocType() throws Exception {
        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();
        docType.setId(count.incrementAndGet());

        // Create the DocType
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocType() throws Exception {
        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();
        docType.setId(count.incrementAndGet());

        // Create the DocType
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocType() throws Exception {
        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();
        docType.setId(count.incrementAndGet());

        // Create the DocType
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(docTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocType() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        int databaseSizeBeforeDelete = docTypeRepository.findAll().size();

        // Delete the docType
        restDocTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, docType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
