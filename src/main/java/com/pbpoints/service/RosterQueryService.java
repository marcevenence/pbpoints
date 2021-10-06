package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.Roster;
import com.pbpoints.repository.RosterRepository;
import com.pbpoints.service.dto.RosterCriteria;
import com.pbpoints.service.dto.RosterDTO;
import com.pbpoints.service.mapper.RosterMapper;
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
 * Service for executing complex queries for {@link Roster} entities in the database.
 * The main input is a {@link RosterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RosterDTO} or a {@link Page} of {@link RosterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RosterQueryService extends QueryService<Roster> {

    private final Logger log = LoggerFactory.getLogger(RosterQueryService.class);

    private final RosterRepository rosterRepository;

    private final RosterMapper rosterMapper;

    public RosterQueryService(RosterRepository rosterRepository, RosterMapper rosterMapper) {
        this.rosterRepository = rosterRepository;
        this.rosterMapper = rosterMapper;
    }

    /**
     * Return a {@link List} of {@link RosterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RosterDTO> findByCriteria(RosterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Roster> specification = createSpecification(criteria);
        return rosterMapper.toDto(rosterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RosterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RosterDTO> findByCriteria(RosterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Roster> specification = createSpecification(criteria);
        return rosterRepository.findAll(specification, page).map(rosterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RosterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Roster> specification = createSpecification(criteria);
        return rosterRepository.count(specification);
    }

    /**
     * Function to convert {@link RosterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Roster> createSpecification(RosterCriteria criteria) {
        Specification<Roster> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Roster_.id));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Roster_.active));
            }
            if (criteria.getPlayerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPlayerId(), root -> root.join(Roster_.players, JoinType.LEFT).get(Player_.id))
                    );
            }
            if (criteria.getTeamId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTeamId(), root -> root.join(Roster_.team, JoinType.LEFT).get(Team_.id))
                    );
            }
            if (criteria.getEventCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventCategoryId(),
                            root -> root.join(Roster_.eventCategory, JoinType.LEFT).get(EventCategory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
