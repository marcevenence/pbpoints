package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.Suspension;
import com.pbpoints.repository.SuspensionRepository;
import com.pbpoints.service.criteria.SuspensionCriteria;
import com.pbpoints.service.dto.SuspensionDTO;
import com.pbpoints.service.mapper.SuspensionMapper;
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
 * Service for executing complex queries for {@link Suspension} entities in the database.
 * The main input is a {@link SuspensionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SuspensionDTO} or a {@link Page} of {@link SuspensionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuspensionQueryService extends QueryService<Suspension> {

    private final Logger log = LoggerFactory.getLogger(SuspensionQueryService.class);

    private final SuspensionRepository suspensionRepository;

    private final SuspensionMapper suspensionMapper;

    public SuspensionQueryService(SuspensionRepository suspensionRepository, SuspensionMapper suspensionMapper) {
        this.suspensionRepository = suspensionRepository;
        this.suspensionMapper = suspensionMapper;
    }

    /**
     * Return a {@link List} of {@link SuspensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SuspensionDTO> findByCriteria(SuspensionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Suspension> specification = createSpecification(criteria);
        return suspensionMapper.toDto(suspensionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SuspensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SuspensionDTO> findByCriteria(SuspensionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Suspension> specification = createSpecification(criteria);
        return suspensionRepository.findAll(specification, page).map(suspensionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuspensionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Suspension> specification = createSpecification(criteria);
        return suspensionRepository.count(specification);
    }

    /**
     * Function to convert {@link SuspensionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Suspension> createSpecification(SuspensionCriteria criteria) {
        Specification<Suspension> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Suspension_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Suspension_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Suspension_.endDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Suspension_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
