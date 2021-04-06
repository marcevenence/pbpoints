package com.pbpoints.repository;

import com.pbpoints.domain.DocType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocTypeRepository extends JpaRepository<DocType, Long> {}
