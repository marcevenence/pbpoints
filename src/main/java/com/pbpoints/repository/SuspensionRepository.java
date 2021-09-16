package com.pbpoints.repository;

import com.pbpoints.domain.Suspension;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Suspension entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuspensionRepository extends JpaRepository<Suspension, Long>, JpaSpecificationExecutor<Suspension> {}
