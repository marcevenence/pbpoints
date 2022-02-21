package com.pbpoints.repository;

import com.pbpoints.domain.PlayerDetailPoint;
import com.pbpoints.domain.PlayerPoint;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlayerDetailPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerDetailPointRepository extends JpaRepository<PlayerDetailPoint, Long>, JpaSpecificationExecutor<PlayerDetailPoint> {
    List<PlayerDetailPoint> findByPlayerPoint(PlayerPoint playerPoint);
}
