package com.pbpoints.repository;

import com.pbpoints.domain.MainRoster;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MainRoster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MainRosterRepository extends JpaRepository<MainRoster, Long>, JpaSpecificationExecutor<MainRoster> {
    @Query("select mainRoster from MainRoster mainRoster where mainRoster.user.login = ?#{principal.username}")
    List<MainRoster> findByUserIsCurrentUser();
}
