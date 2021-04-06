package com.pbpoints.repository;

import com.pbpoints.domain.Team;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {
    @Query("select team from Team team where team.owner.login = ?#{principal.username}")
    List<Team> findByOwnerIsCurrentUser();
}
