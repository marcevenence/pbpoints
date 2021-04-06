package com.pbpoints.repository;

import com.pbpoints.domain.PlayerDetailPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PlayerDetailPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerDetailPointRepository extends JpaRepository<PlayerDetailPoint, Long>, JpaSpecificationExecutor<PlayerDetailPoint> {}
