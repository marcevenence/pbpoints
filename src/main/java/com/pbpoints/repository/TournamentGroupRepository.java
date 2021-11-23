package com.pbpoints.repository;

import com.pbpoints.domain.TournamentGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TournamentGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Long>, JpaSpecificationExecutor<TournamentGroup> {}
