package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.TeamDetailPoint;
import com.pbpoints.repository.TeamDetailPointRepository;
import com.pbpoints.service.criteria.TeamDetailPointCriteria;
import com.pbpoints.service.dto.TeamDetailPointDTO;
import com.pbpoints.service.mapper.TeamDetailPointMapper;
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
 * Service for executing complex queries for {@link TeamDetailPoint} entities in the database.
 * The main input is a {@link TeamDetailPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TeamDetailPointDTO} or a {@link Page} of {@link TeamDetailPointDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeamDetailPointQueryService extends QueryService<TeamDetailPoint> {

    private final Logger log = LoggerFactory.getLogger(TeamDetailPointQueryService.class);

    private final TeamDetailPointRepository teamDetailPointRepository;

    private final TeamDetailPointMapper teamDetailPointMapper;

    public TeamDetailPointQueryService(TeamDetailPointRepository teamDetailPointRepository, TeamDetailPointMapper teamDetailPointMapper) {
        this.teamDetailPointRepository = teamDetailPointRepository;
        this.teamDetailPointMapper = teamDetailPointMapper;
    }

    /**
     * Return a {@link List} of {@link TeamDetailPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamDetailPointDTO> findByCriteria(TeamDetailPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TeamDetailPoint> specification = createSpecification(criteria);
        return teamDetailPointMapper.toDto(teamDetailPointRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TeamDetailPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamDetailPointDTO> findByCriteria(TeamDetailPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TeamDetailPoint> specification = createSpecification(criteria);
        return teamDetailPointRepository.findAll(specification, page).map(teamDetailPointMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TeamDetailPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TeamDetailPoint> specification = createSpecification(criteria);
        return teamDetailPointRepository.count(specification);
    }

    /**
     * Function to convert {@link TeamDetailPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TeamDetailPoint> createSpecification(TeamDetailPointCriteria criteria) {
        Specification<TeamDetailPoint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TeamDetailPoint_.id));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), TeamDetailPoint_.points));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition(), TeamDetailPoint_.position));
            }
            if (criteria.getTeamPointId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTeamPointId(),
                            root -> root.join(TeamDetailPoint_.teamPoint, JoinType.LEFT).get(TeamPoint_.id)
                        )
                    );
            }
            if (criteria.getEventCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventCategoryId(),
                            root -> root.join(TeamDetailPoint_.eventCategory, JoinType.LEFT).get(EventCategory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
