package com.pbpoints.repository;

import com.pbpoints.domain.Season;
import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.enumeration.Status;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Season entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeasonRepository extends JpaRepository<Season, Long>, JpaSpecificationExecutor<Season> {
    Season findByTournamentAndAnio(Tournament tournament, Integer anio);
    List<Season> findByTournamentAndStatus(Tournament tournament, Status status);
}
