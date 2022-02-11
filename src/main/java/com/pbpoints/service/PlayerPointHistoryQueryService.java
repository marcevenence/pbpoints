package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.PlayerPointHistory;
import com.pbpoints.repository.PlayerPointHistoryRepository;
import com.pbpoints.service.criteria.PlayerPointHistoryCriteria;
import com.pbpoints.service.dto.PlayerPointHistoryDTO;
import com.pbpoints.service.mapper.PlayerPointHistoryMapper;
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
 * Service for executing complex queries for {@link PlayerPointHistory} entities in the database.
 * The main input is a {@link PlayerPointHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlayerPointHistoryDTO} or a {@link Page} of {@link PlayerPointHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlayerPointHistoryQueryService extends QueryService<PlayerPointHistory> {

    private final Logger log = LoggerFactory.getLogger(PlayerPointHistoryQueryService.class);

    private final PlayerPointHistoryRepository playerPointHistoryRepository;

    private final PlayerPointHistoryMapper playerPointHistoryMapper;

    public PlayerPointHistoryQueryService(
        PlayerPointHistoryRepository playerPointHistoryRepository,
        PlayerPointHistoryMapper playerPointHistoryMapper
    ) {
        this.playerPointHistoryRepository = playerPointHistoryRepository;
        this.playerPointHistoryMapper = playerPointHistoryMapper;
    }

    /**
     * Return a {@link List} of {@link PlayerPointHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlayerPointHistoryDTO> findByCriteria(PlayerPointHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlayerPointHistory> specification = createSpecification(criteria);
        return playerPointHistoryMapper.toDto(playerPointHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlayerPointHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlayerPointHistoryDTO> findByCriteria(PlayerPointHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlayerPointHistory> specification = createSpecification(criteria);
        return playerPointHistoryRepository.findAll(specification, page).map(playerPointHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlayerPointHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlayerPointHistory> specification = createSpecification(criteria);
        return playerPointHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link PlayerPointHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PlayerPointHistory> createSpecification(PlayerPointHistoryCriteria criteria) {
        Specification<PlayerPointHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            /*if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }*/
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PlayerPointHistory_.id));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), PlayerPointHistory_.points));
            }
            if (criteria.getPlayerPointId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlayerPointId(),
                            root -> root.join(PlayerPointHistory_.playerPoint, JoinType.LEFT).get(PlayerPoint_.id)
                        )
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(PlayerPointHistory_.category, JoinType.LEFT).get(Category_.id)
                        )
                    );
            }
            if (criteria.getSeasonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSeasonId(),
                            root -> root.join(PlayerPointHistory_.season, JoinType.LEFT).get(Season_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
