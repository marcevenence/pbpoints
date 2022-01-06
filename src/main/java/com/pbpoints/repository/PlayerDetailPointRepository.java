package com.pbpoints.repository;

import com.pbpoints.domain.PlayerDetailPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlayerDetailPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerDetailPointRepository extends JpaRepository<PlayerDetailPoint, Long>, JpaSpecificationExecutor<PlayerDetailPoint> {}
