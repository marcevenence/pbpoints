package com.pbpoints.repository;

import com.pbpoints.domain.Season;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Season entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeasonRepository extends JpaRepository<Season, Long>, JpaSpecificationExecutor<Season> {}
