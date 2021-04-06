package com.pbpoints.repository;

import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PlayerPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerPointRepository extends JpaRepository<PlayerPoint, Long>, JpaSpecificationExecutor<PlayerPoint> {
    @Query("select playerPoint from PlayerPoint playerPoint where playerPoint.user.login = ?#{principal.username}")
    List<PlayerPoint> findByUserIsCurrentUser();

    PlayerPoint findByUserAndTournament(User user, Tournament tournament);
}
