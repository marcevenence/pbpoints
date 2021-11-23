package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.TournamentGroup;
import com.pbpoints.repository.TournamentGroupRepository;
import com.pbpoints.service.criteria.TournamentGroupCriteria;
import com.pbpoints.service.dto.TournamentGroupDTO;
import com.pbpoints.service.mapper.TournamentGroupMapper;
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
 * Service for executing complex queries for {@link TournamentGroup} entities in the database.
 * The main input is a {@link TournamentGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TournamentGroupDTO} or a {@link Page} of {@link TournamentGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TournamentGroupQueryService extends QueryService<TournamentGroup> {

    private final Logger log = LoggerFactory.getLogger(TournamentGroupQueryService.class);

    private final TournamentGroupRepository tournamentGroupRepository;

    private final TournamentGroupMapper tournamentGroupMapper;

    public TournamentGroupQueryService(TournamentGroupRepository tournamentGroupRepository, TournamentGroupMapper tournamentGroupMapper) {
        this.tournamentGroupRepository = tournamentGroupRepository;
        this.tournamentGroupMapper = tournamentGroupMapper;
    }

    /**
     * Return a {@link List} of {@link TournamentGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TournamentGroupDTO> findByCriteria(TournamentGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TournamentGroup> specification = createSpecification(criteria);
        return tournamentGroupMapper.toDto(tournamentGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TournamentGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TournamentGroupDTO> findByCriteria(TournamentGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TournamentGroup> specification = createSpecification(criteria);
        return tournamentGroupRepository.findAll(specification, page).map(tournamentGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TournamentGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TournamentGroup> specification = createSpecification(criteria);
        return tournamentGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link TournamentGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TournamentGroup> createSpecification(TournamentGroupCriteria criteria) {
        Specification<TournamentGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TournamentGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TournamentGroup_.name));
            }
            if (criteria.getTournamentAId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentAId(),
                            root -> root.join(TournamentGroup_.tournamentA, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
            if (criteria.getTournamentBId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentBId(),
                            root -> root.join(TournamentGroup_.tournamentB, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
