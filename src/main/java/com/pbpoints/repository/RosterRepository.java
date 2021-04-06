package com.pbpoints.repository;

import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Roster;
import com.pbpoints.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Roster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RosterRepository extends JpaRepository<Roster, Long>, JpaSpecificationExecutor<Roster> {
    List<Roster> findByEventCategory(EventCategory eventCategory);

    Optional<List<Roster>> findByTeam_Owner(User user);

    @Query(
        "select count(1) from Player player, PlayerPoint playerpoint where player.user.id = playerpoint.user.id and player.roster.id = ?1 and playerpoint.category.id = ?2"
    )
    int CountPlayerNextCategory(Long rosterId, Long categoryId);

    @Query("select roster.team.owner.id from Roster roster where roster.id = ?1")
    Long findByRosterId(Long id);
}
