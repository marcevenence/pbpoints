package com.pbpoints.web.rest;

import com.pbpoints.repository.DocTypeRepository;
import com.pbpoints.service.DocTypeService;
import com.pbpoints.service.dto.DocTypeDTO;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.DocType}.
 */
@RestController
@RequestMapping("/api")
public class DocTypeResource {

    private final Logger log = LoggerFactory.getLogger(DocTypeResource.class);

    private static final String ENTITY_NAME = "docType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocTypeService docTypeService;

    private final DocTypeRepository docTypeRepository;

    public DocTypeResource(DocTypeService docTypeService, DocTypeRepository docTypeRepository) {
        this.docTypeService = docTypeService;
        this.docTypeRepository = docTypeRepository;
    }

    /**
     * {@code POST  /doc-types} : Create a new docType.
     *
     * @param docTypeDTO the docTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docTypeDTO, or with status {@code 400 (Bad Request)} if the docType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-types")
    public ResponseEntity<DocTypeDTO> createDocType(@RequestBody DocTypeDTO docTypeDTO) throws URISyntaxException {
        log.debug("REST request to save DocType : {}", docTypeDTO);
        if (docTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new docType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocTypeDTO result = docTypeService.save(docTypeDTO);
        return ResponseEntity
            .created(new URI("/api/doc-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doc-types/:id} : Updates an existing docType.
     *
     * @param id the id of the docTypeDTO to save.
     * @param docTypeDTO the docTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docTypeDTO,
     * or with status {@code 400 (Bad Request)} if the docTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doc-types/{id}")
    public ResponseEntity<DocTypeDTO> updateDocType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocTypeDTO docTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocType : {}, {}", id, docTypeDTO);
        if (docTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocTypeDTO result = docTypeService.save(docTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doc-types/:id} : Partial updates given fields of an existing docType, field will ignore if it is null
     *
     * @param id the id of the docTypeDTO to save.
     * @param docTypeDTO the docTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docTypeDTO,
     * or with status {@code 400 (Bad Request)} if the docTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the docTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the docTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doc-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DocTypeDTO> partialUpdateDocType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocTypeDTO docTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocType partially : {}, {}", id, docTypeDTO);
        if (docTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocTypeDTO> result = docTypeService.partialUpdate(docTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /doc-types} : get all the docTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docTypes in body.
     */
    @GetMapping("/doc-types")
    public ResponseEntity<List<DocTypeDTO>> getAllDocTypes(Pageable pageable) {
        log.debug("REST request to get a page of DocTypes");
        Page<DocTypeDTO> page = docTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-types/:id} : get the "id" docType.
     *
     * @param id the id of the docTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-types/{id}")
    public ResponseEntity<DocTypeDTO> getDocType(@PathVariable Long id) {
        log.debug("REST request to get DocType : {}", id);
        Optional<DocTypeDTO> docTypeDTO = docTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docTypeDTO);
    }

    /**
     * {@code DELETE  /doc-types/:id} : delete the "id" docType.
     *
     * @param id the id of the docTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doc-types/{id}")
    public ResponseEntity<Void> deleteDocType(@PathVariable Long id) {
        log.debug("REST request to delete DocType : {}", id);
        docTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
