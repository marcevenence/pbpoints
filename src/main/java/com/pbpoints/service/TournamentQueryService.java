package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.Tournament;
import com.pbpoints.repository.TournamentRepository;
import com.pbpoints.service.criteria.TournamentCriteria;
import com.pbpoints.service.dto.TournamentDTO;
import com.pbpoints.service.mapper.TournamentMapper;
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
 * Service for executing complex queries for {@link Tournament} entities in the database.
 * The main input is a {@link TournamentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TournamentDTO} or a {@link Page} of {@link TournamentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TournamentQueryService extends QueryService<Tournament> {

    private final Logger log = LoggerFactory.getLogger(TournamentQueryService.class);

    private final TournamentRepository tournamentRepository;

    private final TournamentMapper tournamentMapper;

    public TournamentQueryService(TournamentRepository tournamentRepository, TournamentMapper tournamentMapper) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentMapper = tournamentMapper;
    }

    /**
     * Return a {@link List} of {@link TournamentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TournamentDTO> findByCriteria(TournamentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tournament> specification = createSpecification(criteria);
        return tournamentMapper.toDto(tournamentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TournamentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TournamentDTO> findByCriteria(TournamentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tournament> specification = createSpecification(criteria);
        return tournamentRepository.findAll(specification, page).map(tournamentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TournamentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tournament> specification = createSpecification(criteria);
        return tournamentRepository.count(specification);
    }

    /**
     * Function to convert {@link TournamentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tournament> createSpecification(TournamentCriteria criteria) {
        Specification<Tournament> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tournament_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Tournament_.name));
            }
            if (criteria.getCloseInscrDays() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCloseInscrDays(), Tournament_.closeInscrDays));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Tournament_.status));
            }
            if (criteria.getCategorize() != null) {
                specification = specification.and(buildSpecification(criteria.getCategorize(), Tournament_.categorize));
            }
            if (criteria.getCantPlayersNextCategory() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCantPlayersNextCategory(), Tournament_.cantPlayersNextCategory));
            }
            if (criteria.getQtyTeamGroups() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQtyTeamGroups(), Tournament_.qtyTeamGroups));
            }
            if (criteria.getStartSeason() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartSeason(), Tournament_.startSeason));
            }
            if (criteria.getEndSeason() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndSeason(), Tournament_.endSeason));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(Tournament_.events, JoinType.LEFT).get(Event_.id))
                    );
            }
            if (criteria.getOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnerId(), root -> root.join(Tournament_.owner, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
