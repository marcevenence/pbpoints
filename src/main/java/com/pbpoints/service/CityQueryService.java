package com.pbpoints.service;

import com.pbpoints.domain.*; // for static metamodels
import com.pbpoints.domain.City;
import com.pbpoints.repository.CityRepository;
import com.pbpoints.service.dto.CityCriteria;
import com.pbpoints.service.dto.CityDTO;
import com.pbpoints.service.mapper.CityMapper;
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
 * Service for executing complex queries for {@link City} entities in the database.
 * The main input is a {@link CityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CityDTO} or a {@link Page} of {@link CityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CityQueryService extends QueryService<City> {

    private final Logger log = LoggerFactory.getLogger(CityQueryService.class);

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    public CityQueryService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    /**
     * Return a {@link List} of {@link CityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CityDTO> findByCriteria(CityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<City> specification = createSpecification(criteria);
        return cityMapper.toDto(cityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CityDTO> findByCriteria(CityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<City> specification = createSpecification(criteria);
        return cityRepository.findAll(specification, page).map(cityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<City> specification = createSpecification(criteria);
        return cityRepository.count(specification);
    }

    /**
     * Function to convert {@link CityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<City> createSpecification(CityCriteria criteria) {
        Specification<City> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), City_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), City_.name));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), City_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), City_.longitude));
            }
            if (criteria.getProvinceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProvinceId(), root -> root.join(City_.province, JoinType.LEFT).get(Province_.id))
                    );
            }
        }
        return specification;
    }
}
