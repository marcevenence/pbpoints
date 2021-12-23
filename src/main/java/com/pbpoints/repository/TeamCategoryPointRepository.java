package com.pbpoints.repository;

import com.pbpoints.domain.TeamCategoryPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TeamCategoryPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamCategoryPointRepository extends JpaRepository<TeamCategoryPoint, Long>, JpaSpecificationExecutor<TeamCategoryPoint> {}
