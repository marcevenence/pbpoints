package com.pbpoints.repository;

import com.pbpoints.domain.MainRoster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MainRoster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MainRosterRepository extends JpaRepository<MainRoster, Long>, JpaSpecificationExecutor<MainRoster> {}
