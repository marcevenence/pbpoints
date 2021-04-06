package com.pbpoints.repository;

import com.pbpoints.domain.Tournament;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tournament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>, JpaSpecificationExecutor<Tournament> {
    @Query("select tournament from Tournament tournament where tournament.owner.login = ?#{principal.username}")
    List<Tournament> findByOwnerIsCurrentUser();
}
