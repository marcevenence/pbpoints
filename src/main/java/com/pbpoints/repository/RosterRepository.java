package com.pbpoints.repository;

import com.pbpoints.domain.Roster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Roster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RosterRepository extends JpaRepository<Roster, Long>, JpaSpecificationExecutor<Roster> {}
