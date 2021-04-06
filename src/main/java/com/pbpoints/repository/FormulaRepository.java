package com.pbpoints.repository;

import com.pbpoints.domain.Formula;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Formula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormulaRepository extends JpaRepository<Formula, Long>, JpaSpecificationExecutor<Formula> {}
