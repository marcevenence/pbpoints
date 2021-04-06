package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.Formula;
import com.pbpoints.repository.FormulaRepository;
import com.pbpoints.service.dto.FormulaCriteria;
import com.pbpoints.service.dto.FormulaDTO;
import com.pbpoints.service.mapper.FormulaMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Formula} entities in the database.
 * The main input is a {@link FormulaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormulaDTO} or a {@link Page} of {@link FormulaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormulaQueryService extends QueryService<Formula> {

    private final Logger log = LoggerFactory.getLogger(FormulaQueryService.class);

    private final FormulaRepository formulaRepository;

    private final FormulaMapper formulaMapper;

    public FormulaQueryService(FormulaRepository formulaRepository, FormulaMapper formulaMapper) {
        this.formulaRepository = formulaRepository;
        this.formulaMapper = formulaMapper;
    }

    /**
     * Return a {@link List} of {@link FormulaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormulaDTO> findByCriteria(FormulaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Formula> specification = createSpecification(criteria);
        return formulaMapper.toDto(formulaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FormulaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormulaDTO> findByCriteria(FormulaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Formula> specification = createSpecification(criteria);
        return formulaRepository.findAll(specification, page).map(formulaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormulaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Formula> specification = createSpecification(criteria);
        return formulaRepository.count(specification);
    }

    /**
     * Function to convert {@link FormulaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Formula> createSpecification(FormulaCriteria criteria) {
        Specification<Formula> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Formula_.id));
            }
            if (criteria.getFormula() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFormula(), Formula_.formula));
            }
            if (criteria.getVar1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVar1(), Formula_.var1));
            }
            if (criteria.getVar2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVar2(), Formula_.var2));
            }
            if (criteria.getVar3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVar3(), Formula_.var3));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Formula_.description));
            }
            if (criteria.getExample() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExample(), Formula_.example));
            }
            if (criteria.getTournamentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentId(),
                            root -> root.join(Formula_.tournament, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
