package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.Sponsor;
import com.pbpoints.repository.SponsorRepository;
import com.pbpoints.service.criteria.SponsorCriteria;
import com.pbpoints.service.dto.SponsorDTO;
import com.pbpoints.service.mapper.SponsorMapper;
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
 * Service for executing complex queries for {@link Sponsor} entities in the database.
 * The main input is a {@link SponsorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SponsorDTO} or a {@link Page} of {@link SponsorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SponsorQueryService extends QueryService<Sponsor> {

    private final Logger log = LoggerFactory.getLogger(SponsorQueryService.class);

    private final SponsorRepository sponsorRepository;

    private final SponsorMapper sponsorMapper;

    public SponsorQueryService(SponsorRepository sponsorRepository, SponsorMapper sponsorMapper) {
        this.sponsorRepository = sponsorRepository;
        this.sponsorMapper = sponsorMapper;
    }

    /**
     * Return a {@link List} of {@link SponsorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SponsorDTO> findByCriteria(SponsorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sponsor> specification = createSpecification(criteria);
        return sponsorMapper.toDto(sponsorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SponsorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SponsorDTO> findByCriteria(SponsorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sponsor> specification = createSpecification(criteria);
        return sponsorRepository.findAll(specification, page).map(sponsorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SponsorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sponsor> specification = createSpecification(criteria);
        return sponsorRepository.count(specification);
    }

    /**
     * Function to convert {@link SponsorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sponsor> createSpecification(SponsorCriteria criteria) {
        Specification<Sponsor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sponsor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Sponsor_.name));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Sponsor_.active));
            }
            if (criteria.getTournamentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTournamentId(),
                            root -> root.join(Sponsor_.tournament, JoinType.LEFT).get(Tournament_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
