package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.TeamCategoryPoint;
import com.pbpoints.repository.TeamCategoryPointRepository;
import com.pbpoints.service.criteria.TeamCategoryPointCriteria;
import com.pbpoints.service.dto.TeamCategoryPointDTO;
import com.pbpoints.service.mapper.TeamCategoryPointMapper;
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
 * Service for executing complex queries for {@link TeamCategoryPoint} entities in the database.
 * The main input is a {@link TeamCategoryPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TeamCategoryPointDTO} or a {@link Page} of {@link TeamCategoryPointDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeamCategoryPointQueryService extends QueryService<TeamCategoryPoint> {

    private final Logger log = LoggerFactory.getLogger(TeamCategoryPointQueryService.class);

    private final TeamCategoryPointRepository teamCategoryPointRepository;

    private final TeamCategoryPointMapper teamCategoryPointMapper;

    public TeamCategoryPointQueryService(
        TeamCategoryPointRepository teamCategoryPointRepository,
        TeamCategoryPointMapper teamCategoryPointMapper
    ) {
        this.teamCategoryPointRepository = teamCategoryPointRepository;
        this.teamCategoryPointMapper = teamCategoryPointMapper;
    }

    /**
     * Return a {@link List} of {@link TeamCategoryPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamCategoryPointDTO> findByCriteria(TeamCategoryPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TeamCategoryPoint> specification = createSpecification(criteria);
        return teamCategoryPointMapper.toDto(teamCategoryPointRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TeamCategoryPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamCategoryPointDTO> findByCriteria(TeamCategoryPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TeamCategoryPoint> specification = createSpecification(criteria);
        return teamCategoryPointRepository.findAll(specification, page).map(teamCategoryPointMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TeamCategoryPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TeamCategoryPoint> specification = createSpecification(criteria);
        return teamCategoryPointRepository.count(specification);
    }

    /**
     * Function to convert {@link TeamCategoryPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TeamCategoryPoint> createSpecification(TeamCategoryPointCriteria criteria) {
        Specification<TeamCategoryPoint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TeamCategoryPoint_.id));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), TeamCategoryPoint_.points));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition(), TeamCategoryPoint_.position));
            }
            if (criteria.getTeamDetailPointId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTeamDetailPointId(),
                            root -> root.join(TeamCategoryPoint_.teamDetailPoint, JoinType.LEFT).get(TeamDetailPoint_.id)
                        )
                    );
            }
            if (criteria.getEventCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventCategoryId(),
                            root -> root.join(TeamCategoryPoint_.eventCategory, JoinType.LEFT).get(EventCategory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
