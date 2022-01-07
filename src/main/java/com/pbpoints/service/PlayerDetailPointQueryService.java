package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.PlayerDetailPoint;
import com.pbpoints.repository.PlayerDetailPointRepository;
import com.pbpoints.service.criteria.PlayerDetailPointCriteria;
import com.pbpoints.service.dto.PlayerDetailPointDTO;
import com.pbpoints.service.mapper.PlayerDetailPointMapper;
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
 * Service for executing complex queries for {@link PlayerDetailPoint} entities in the database.
 * The main input is a {@link PlayerDetailPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlayerDetailPointDTO} or a {@link Page} of {@link PlayerDetailPointDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlayerDetailPointQueryService extends QueryService<PlayerDetailPoint> {

    private final Logger log = LoggerFactory.getLogger(PlayerDetailPointQueryService.class);

    private final PlayerDetailPointRepository playerDetailPointRepository;

    private final PlayerDetailPointMapper playerDetailPointMapper;

    public PlayerDetailPointQueryService(
        PlayerDetailPointRepository playerDetailPointRepository,
        PlayerDetailPointMapper playerDetailPointMapper
    ) {
        this.playerDetailPointRepository = playerDetailPointRepository;
        this.playerDetailPointMapper = playerDetailPointMapper;
    }

    /**
     * Return a {@link List} of {@link PlayerDetailPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlayerDetailPointDTO> findByCriteria(PlayerDetailPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlayerDetailPoint> specification = createSpecification(criteria);
        return playerDetailPointMapper.toDto(playerDetailPointRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlayerDetailPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlayerDetailPointDTO> findByCriteria(PlayerDetailPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlayerDetailPoint> specification = createSpecification(criteria);
        return playerDetailPointRepository.findAll(specification, page).map(playerDetailPointMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlayerDetailPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlayerDetailPoint> specification = createSpecification(criteria);
        return playerDetailPointRepository.count(specification);
    }

    /**
     * Function to convert {@link PlayerDetailPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PlayerDetailPoint> createSpecification(PlayerDetailPointCriteria criteria) {
        Specification<PlayerDetailPoint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PlayerDetailPoint_.id));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), PlayerDetailPoint_.points));
            }
            if (criteria.getPlayerPointId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlayerPointId(),
                            root -> root.join(PlayerDetailPoint_.playerPoint, JoinType.LEFT).get(PlayerPoint_.id)
                        )
                    );
            }
            if (criteria.getEventCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventCategoryId(),
                            root -> root.join(PlayerDetailPoint_.eventCategory, JoinType.LEFT).get(EventCategory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
