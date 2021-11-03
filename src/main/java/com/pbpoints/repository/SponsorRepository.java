package com.pbpoints.repository;

import com.pbpoints.domain.EventCategory;
import com.pbpoints.domain.Game;
import com.pbpoints.domain.Sponsor;
import com.pbpoints.domain.Tournament;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sponsor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long>, JpaSpecificationExecutor<Sponsor> {
    public List<Sponsor> findByTournament(Tournament tournament);
}
