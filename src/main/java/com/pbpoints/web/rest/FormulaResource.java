package com.pbpoints.web.rest;

import com.pbpoints.repository.FormulaRepository;
import com.pbpoints.service.FormulaQueryService;
import com.pbpoints.service.FormulaService;
import com.pbpoints.service.criteria.FormulaCriteria;
import com.pbpoints.service.dto.FormulaDTO;
import com.pbpoints.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pbpoints.domain.Formula}.
 */
@RestController
@RequestMapping("/api")
public class FormulaResource {

    private final Logger log = LoggerFactory.getLogger(FormulaResource.class);

    private static final String ENTITY_NAME = "formula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormulaService formulaService;

    private final FormulaRepository formulaRepository;

    private final FormulaQueryService formulaQueryService;

    public FormulaResource(FormulaService formulaService, FormulaRepository formulaRepository, FormulaQueryService formulaQueryService) {
        this.formulaService = formulaService;
        this.formulaRepository = formulaRepository;
        this.formulaQueryService = formulaQueryService;
    }

    /**
     * {@code POST  /formulas} : Create a new formula.
     *
     * @param formulaDTO the formulaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formulaDTO, or with status {@code 400 (Bad Request)} if the formula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formulas")
    public ResponseEntity<FormulaDTO> createFormula(@Valid @RequestBody FormulaDTO formulaDTO) throws URISyntaxException {
        log.debug("REST request to save Formula : {}", formulaDTO);
        if (formulaDTO.getId() != null) {
            throw new BadRequestAlertException("A new formula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormulaDTO result = formulaService.save(formulaDTO);
        return ResponseEntity
            .created(new URI("/api/formulas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formulas/:id} : Updates an existing formula.
     *
     * @param id the id of the formulaDTO to save.
     * @param formulaDTO the formulaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formulaDTO,
     * or with status {@code 400 (Bad Request)} if the formulaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formulaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formulas/{id}")
    public ResponseEntity<FormulaDTO> updateFormula(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FormulaDTO formulaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Formula : {}, {}", id, formulaDTO);
        if (formulaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formulaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormulaDTO result = formulaService.save(formulaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formulaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formulas/:id} : Partial updates given fields of an existing formula, field will ignore if it is null
     *
     * @param id the id of the formulaDTO to save.
     * @param formulaDTO the formulaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formulaDTO,
     * or with status {@code 400 (Bad Request)} if the formulaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formulaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formulaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/formulas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FormulaDTO> partialUpdateFormula(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FormulaDTO formulaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Formula partially : {}, {}", id, formulaDTO);
        if (formulaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formulaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormulaDTO> result = formulaService.partialUpdate(formulaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formulaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formulas} : get all the formulas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formulas in body.
     */
    @GetMapping("/formulas")
    public ResponseEntity<List<FormulaDTO>> getAllFormulas(FormulaCriteria criteria) {
        log.debug("REST request to get Formulas by criteria: {}", criteria);
        List<FormulaDTO> entityList = formulaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /formulas/count} : count all the formulas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/formulas/count")
    public ResponseEntity<Long> countFormulas(FormulaCriteria criteria) {
        log.debug("REST request to count Formulas by criteria: {}", criteria);
        return ResponseEntity.ok().body(formulaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /formulas/:id} : get the "id" formula.
     *
     * @param id the id of the formulaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formulaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formulas/{id}")
    public ResponseEntity<FormulaDTO> getFormula(@PathVariable Long id) {
        log.debug("REST request to get Formula : {}", id);
        Optional<FormulaDTO> formulaDTO = formulaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formulaDTO);
    }

    /**
     * {@code DELETE  /formulas/:id} : delete the "id" formula.
     *
     * @param id the id of the formulaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formulas/{id}")
    public ResponseEntity<Void> deleteFormula(@PathVariable Long id) {
        log.debug("REST request to delete Formula : {}", id);
        formulaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
