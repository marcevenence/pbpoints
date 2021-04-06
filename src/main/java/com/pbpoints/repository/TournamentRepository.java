package com.pbpoints.repository;

import com.pbpoints.domain.Event;
import com.pbpoints.domain.Tournament;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Tournament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>, JpaSpecificationExecutor<Tournament> {
    @Query("select tournament from Tournament tournament where tournament.owner.login = ?#{principal.username}")
    List<Tournament> findByOwnerIsCurrentUser();

    Tournament findByEvents(Event event);
}
