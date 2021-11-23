package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.Field;
import com.pbpoints.repository.FieldRepository;
import com.pbpoints.service.criteria.FieldCriteria;
import com.pbpoints.service.dto.FieldDTO;
import com.pbpoints.service.mapper.FieldMapper;
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
 * Service for executing complex queries for {@link Field} entities in the database.
 * The main input is a {@link FieldCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FieldDTO} or a {@link Page} of {@link FieldDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FieldQueryService extends QueryService<Field> {

    private final Logger log = LoggerFactory.getLogger(FieldQueryService.class);

    private final FieldRepository fieldRepository;

    private final FieldMapper fieldMapper;

    public FieldQueryService(FieldRepository fieldRepository, FieldMapper fieldMapper) {
        this.fieldRepository = fieldRepository;
        this.fieldMapper = fieldMapper;
    }

    /**
     * Return a {@link List} of {@link FieldDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FieldDTO> findByCriteria(FieldCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Field> specification = createSpecification(criteria);
        return fieldMapper.toDto(fieldRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FieldDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FieldDTO> findByCriteria(FieldCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Field> specification = createSpecification(criteria);
        return fieldRepository.findAll(specification, page).map(fieldMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FieldCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Field> specification = createSpecification(criteria);
        return fieldRepository.count(specification);
    }

    /**
     * Function to convert {@link FieldCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Field> createSpecification(FieldCriteria criteria) {
        Specification<Field> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Field_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Field_.name));
            }
            if (criteria.getCityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCityId(), root -> root.join(Field_.city, JoinType.LEFT).get(City_.id))
                    );
            }
        }
        return specification;
    }
}
