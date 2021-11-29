package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.Equipment;
import com.pbpoints.repository.EquipmentRepository;
import com.pbpoints.service.criteria.EquipmentCriteria;
import com.pbpoints.service.dto.EquipmentDTO;
import com.pbpoints.service.mapper.EquipmentMapper;
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
 * Service for executing complex queries for {@link Equipment} entities in the database.
 * The main input is a {@link EquipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipmentDTO} or a {@link Page} of {@link EquipmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentQueryService extends QueryService<Equipment> {

    private final Logger log = LoggerFactory.getLogger(EquipmentQueryService.class);

    private final EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper;

    public EquipmentQueryService(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
    }

    /**
     * Return a {@link List} of {@link EquipmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentDTO> findByCriteria(EquipmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentMapper.toDto(equipmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentDTO> findByCriteria(EquipmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentRepository.findAll(specification, page).map(equipmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Equipment> createSpecification(EquipmentCriteria criteria) {
        Specification<Equipment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Equipment_.id));
            }
            if (criteria.getBrand() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrand(), Equipment_.brand));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Equipment_.model));
            }
            if (criteria.getSerial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerial(), Equipment_.serial));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Equipment_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
