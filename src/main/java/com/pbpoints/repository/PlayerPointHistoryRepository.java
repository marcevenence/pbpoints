package com.pbpoints.repository;

import com.pbpoints.domain.PlayerPoint;
import com.pbpoints.domain.PlayerPointHistory;
import com.pbpoints.domain.Season;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlayerPointHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerPointHistoryRepository
    extends JpaRepository<PlayerPointHistory, Long>, JpaSpecificationExecutor<PlayerPointHistory> {
    PlayerPointHistory findByPlayerPointAndSeason(PlayerPoint playerPoint, Season season);
}
