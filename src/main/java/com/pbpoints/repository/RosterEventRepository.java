package com.pbpoints.repository;

import com.pbpoints.domain.RosterEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RosterEventRepository extends JpaRepository<RosterEvent, Long> {
    List<RosterEvent> findByEvCatId(Long evCatId);
    List<RosterEvent> findByTournamentId(Long tournamentId);

    @Query("select pbPointId from RosterEvent rosterEvent where rosterEvent.tournamentId = ?1 group by pbPointId")
    List<Long> findUsersByTournamentId(Long tournamentId);

    @Query(
        "select rosterEvent from RosterEvent rosterEvent where rosterEvent.tournamentId = ?1 and rosterEvent.pbPointId = ?2 and rosterEvent.anio = ?3"
    )
    List<RosterEvent> findPlayersByTournamentIdAndPlayerIdAndAnio(Long tournamentId, Long userId, Integer anio);

    @Query("select count(rosterEvent) from RosterEvent rosterEvent where rosterEvent.pbPointId = ?1 and rosterEvent.eventId = ?2")
    List<Long> countByUserIdAndEventId(Long userId, Long eventId);
}
