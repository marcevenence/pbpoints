package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.Game;
import com.pbpoints.repository.GameRepository;
import com.pbpoints.service.dto.GameCriteria;
import com.pbpoints.service.dto.GameDTO;
import com.pbpoints.service.mapper.GameMapper;
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
 * Service for executing complex queries for {@link Game} entities in the database.
 * The main input is a {@link GameCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GameDTO} or a {@link Page} of {@link GameDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GameQueryService extends QueryService<Game> {

    private final Logger log = LoggerFactory.getLogger(GameQueryService.class);

    private final GameRepository gameRepository;

    private final GameMapper gameMapper;

    public GameQueryService(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    /**
     * Return a {@link List} of {@link GameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GameDTO> findByCriteria(GameCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Game> specification = createSpecification(criteria);
        return gameMapper.toDto(gameRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GameDTO> findByCriteria(GameCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Game> specification = createSpecification(criteria);
        return gameRepository.findAll(specification, page).map(gameMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GameCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Game> specification = createSpecification(criteria);
        return gameRepository.count(specification);
    }

    /**
     * Function to convert {@link GameCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Game> createSpecification(GameCriteria criteria) {
        Specification<Game> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Game_.id));
            }
            if (criteria.getPointsA() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPointsA(), Game_.pointsA));
            }
            if (criteria.getPointsB() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPointsB(), Game_.pointsB));
            }
            if (criteria.getSplitDeckNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSplitDeckNum(), Game_.splitDeckNum));
            }
            if (criteria.getTimeLeft() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeLeft(), Game_.timeLeft));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Game_.status));
            }
            if (criteria.getOvertimeA() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOvertimeA(), Game_.overtimeA));
            }
            if (criteria.getOvertimeB() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOvertimeB(), Game_.overtimeB));
            }
            if (criteria.getUvuA() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUvuA(), Game_.uvuA));
            }
            if (criteria.getUvuB() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUvuB(), Game_.uvuB));
            }
            if (criteria.getGroup() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGroup(), Game_.group));
            }
            if (criteria.getClasif() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClasif(), Game_.clasif));
            }
            if (criteria.getTeamAId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTeamAId(), root -> root.join(Game_.teamA, JoinType.LEFT).get(Team_.id))
                    );
            }
            if (criteria.getTeamBId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTeamBId(), root -> root.join(Game_.teamB, JoinType.LEFT).get(Team_.id))
                    );
            }
            if (criteria.getEventCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventCategoryId(),
                            root -> root.join(Game_.eventCategory, JoinType.LEFT).get(EventCategory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
