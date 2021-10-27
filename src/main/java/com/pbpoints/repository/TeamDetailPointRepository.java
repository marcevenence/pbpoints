package com.pbpoints.repository;

import com.pbpoints.domain.TeamDetailPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TeamDetailPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamDetailPointRepository extends JpaRepository<TeamDetailPoint, Long>, JpaSpecificationExecutor<TeamDetailPoint> {}
