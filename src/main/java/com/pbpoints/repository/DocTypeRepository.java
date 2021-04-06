package com.pbpoints.repository;

import com.pbpoints.domain.DocType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DocType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocTypeRepository extends JpaRepository<DocType, Long> {}
