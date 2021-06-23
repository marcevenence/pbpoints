package com.pbpoints.repository;

import com.pbpoints.domain.Team;
import java.util.List;
import java.util.Optional;
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

    Optional<Team> findByName(String name);

    @Query("select team from Team team where team.name = ?1 and team.owner.id = ?2")
    Optional<Team> findByNameAndIdOwner(String name, Long ownerId);
}
