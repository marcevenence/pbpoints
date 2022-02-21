package com.pbpoints.repository;

import com.pbpoints.domain.Event;
import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Season;
import com.pbpoints.domain.Tournament;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Event entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Optional<List<Event>> findByEndDate(LocalDate localDate);
    List<Event> findByTournamentAndSeason(Tournament tournament, Season season);
}
