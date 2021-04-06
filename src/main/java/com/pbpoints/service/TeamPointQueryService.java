package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.TeamPoint;
import com.pbpoints.repository.TeamPointRepository;
import com.pbpoints.service.criteria.TeamPointCriteria;
import com.pbpoints.service.dto.TeamPointDTO;
import com.pbpoints.service.mapper.TeamPointMapper;
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
 * Service for executing complex queries for {@link TeamPoint} entities in the database.
 * The main input is a {@link TeamPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TeamPointDTO} or a {@link Page} of {@link TeamPointDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeamPointQueryService extends QueryService<TeamPoint> {

    private final Logger log = LoggerFactory.getLogger(TeamPointQueryService.class);

    private final TeamPointRepository teamPointRepository;

    private final TeamPointMapper teamPointMapper;

    public TeamPointQueryService(TeamPointRepository teamPointRepository, TeamPointMapper teamPointMapper) {
        this.teamPointRepository = teamPointRepository;
        this.teamPointMapper = teamPointMapper;
    }

    /**
     * Return a {@link List} of {@link TeamPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamPointDTO> findByCriteria(TeamPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TeamPoint> specification = createSpecification(criteria);
        return teamPointMapper.toDto(teamPointRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TeamPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamPointDTO> findByCriteria(TeamPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TeamPoint> specification = createSpecification(criteria);
        return teamPointRepository.findAll(specification, page).map(teamPointMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TeamPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TeamPoint> specification = createSpecification(criteria);
        return teamPointRepository.count(specification);
    }

    /**
     * Function to convert {@link TeamPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TeamPoint> createSpecification(TeamPointCriteria criteria) {
        Specification<TeamPoint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TeamPoint_.id));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), TeamPoint_.points));
            }
            if (criteria.getTeamId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTeamId(), root -> root.join(TeamPoint_.team, JoinType.LEFT).get(Team_.id))
                    );
            }
            if (criteria.getTournamentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentId(),
                            root -> root.join(TeamPoint_.tournament, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
