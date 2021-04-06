package com.pbpoints.repository;

import com.pbpoints.domain.Player;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Player entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {
    @Query("select player from Player player where player.user.login = ?#{principal.username}")
    List<Player> findByUserIsCurrentUser();
}
