package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.repository.PlayerPointRepository;
import com.pbpoints.service.dto.PlayerPointCriteria;
import com.pbpoints.service.dto.PlayerPointDTO;
import com.pbpoints.service.mapper.PlayerPointMapper;
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
 * Service for executing complex queries for {@link PlayerPoint} entities in the database.
 * The main input is a {@link PlayerPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlayerPointDTO} or a {@link Page} of {@link PlayerPointDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlayerPointQueryService extends QueryService<PlayerPoint> {

    private final Logger log = LoggerFactory.getLogger(PlayerPointQueryService.class);

    private final PlayerPointRepository playerPointRepository;

    private final PlayerPointMapper playerPointMapper;

    public PlayerPointQueryService(PlayerPointRepository playerPointRepository, PlayerPointMapper playerPointMapper) {
        this.playerPointRepository = playerPointRepository;
        this.playerPointMapper = playerPointMapper;
    }

    /**
     * Return a {@link List} of {@link PlayerPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlayerPointDTO> findByCriteria(PlayerPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlayerPoint> specification = createSpecification(criteria);
        return playerPointMapper.toDto(playerPointRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlayerPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlayerPointDTO> findByCriteria(PlayerPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlayerPoint> specification = createSpecification(criteria);
        return playerPointRepository.findAll(specification, page).map(playerPointMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlayerPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlayerPoint> specification = createSpecification(criteria);
        return playerPointRepository.count(specification);
    }

    /**
     * Function to convert {@link PlayerPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PlayerPoint> createSpecification(PlayerPointCriteria criteria) {
        Specification<PlayerPoint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PlayerPoint_.id));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), PlayerPoint_.points));
            }
            if (criteria.getTournamentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentId(),
                            root -> root.join(PlayerPoint_.tournament, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(PlayerPoint_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(PlayerPoint_.category, JoinType.LEFT).get(Category_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
