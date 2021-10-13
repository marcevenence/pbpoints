package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.MainRoster;
import com.pbpoints.repository.MainRosterRepository;
import com.pbpoints.service.criteria.MainRosterCriteria;
import com.pbpoints.service.dto.MainRosterDTO;
import com.pbpoints.service.mapper.MainRosterMapper;
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
 * Service for executing complex queries for {@link MainRoster} entities in the database.
 * The main input is a {@link MainRosterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MainRosterDTO} or a {@link Page} of {@link MainRosterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MainRosterQueryService extends QueryService<MainRoster> {

    private final Logger log = LoggerFactory.getLogger(MainRosterQueryService.class);

    private final MainRosterRepository mainRosterRepository;

    private final MainRosterMapper mainRosterMapper;

    public MainRosterQueryService(MainRosterRepository mainRosterRepository, MainRosterMapper mainRosterMapper) {
        this.mainRosterRepository = mainRosterRepository;
        this.mainRosterMapper = mainRosterMapper;
    }

    /**
     * Return a {@link List} of {@link MainRosterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MainRosterDTO> findByCriteria(MainRosterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MainRoster> specification = createSpecification(criteria);
        return mainRosterMapper.toDto(mainRosterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MainRosterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MainRosterDTO> findByCriteria(MainRosterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MainRoster> specification = createSpecification(criteria);
        return mainRosterRepository.findAll(specification, page).map(mainRosterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MainRosterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MainRoster> specification = createSpecification(criteria);
        return mainRosterRepository.count(specification);
    }

    /**
     * Function to convert {@link MainRosterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MainRoster> createSpecification(MainRosterCriteria criteria) {
        Specification<MainRoster> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MainRoster_.id));
            }
            if (criteria.getTeamId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTeamId(), root -> root.join(MainRoster_.team, JoinType.LEFT).get(Team_.id))
                    );
            }
            if (criteria.getUserExtraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserExtraId(),
                            root -> root.join(MainRoster_.userExtra, JoinType.LEFT).get(UserExtra_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
