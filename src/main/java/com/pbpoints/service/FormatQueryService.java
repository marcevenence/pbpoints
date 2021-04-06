package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.Format;
import com.pbpoints.repository.FormatRepository;
import com.pbpoints.service.dto.FormatCriteria;
import com.pbpoints.service.dto.FormatDTO;
import com.pbpoints.service.mapper.FormatMapper;
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
 * Service for executing complex queries for {@link Format} entities in the database.
 * The main input is a {@link FormatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormatDTO} or a {@link Page} of {@link FormatDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormatQueryService extends QueryService<Format> {

    private final Logger log = LoggerFactory.getLogger(FormatQueryService.class);

    private final FormatRepository formatRepository;

    private final FormatMapper formatMapper;

    public FormatQueryService(FormatRepository formatRepository, FormatMapper formatMapper) {
        this.formatRepository = formatRepository;
        this.formatMapper = formatMapper;
    }

    /**
     * Return a {@link List} of {@link FormatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormatDTO> findByCriteria(FormatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Format> specification = createSpecification(criteria);
        return formatMapper.toDto(formatRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FormatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormatDTO> findByCriteria(FormatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Format> specification = createSpecification(criteria);
        return formatRepository.findAll(specification, page).map(formatMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormatCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Format> specification = createSpecification(criteria);
        return formatRepository.count(specification);
    }

    /**
     * Function to convert {@link FormatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Format> createSpecification(FormatCriteria criteria) {
        Specification<Format> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Format_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Format_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Format_.description));
            }
            if (criteria.getCoeficient() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCoeficient(), Format_.coeficient));
            }
            if (criteria.getPlayersQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayersQty(), Format_.playersQty));
            }
            if (criteria.getTournamentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentId(),
                            root -> root.join(Format_.tournament, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
