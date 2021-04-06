package com.pbpoints.repository;

import com.pbpoints.domain.TeamPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TeamPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamPointRepository extends JpaRepository<TeamPoint, Long>, JpaSpecificationExecutor<TeamPoint> {}
