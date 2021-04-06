package com.pbpoints.repository;

import com.pbpoints.domain.PlayerPoint;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlayerPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerPointRepository extends JpaRepository<PlayerPoint, Long>, JpaSpecificationExecutor<PlayerPoint> {
    @Query("select playerPoint from PlayerPoint playerPoint where playerPoint.user.login = ?#{principal.username}")
    List<PlayerPoint> findByUserIsCurrentUser();
}
