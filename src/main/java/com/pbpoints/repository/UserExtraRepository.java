package com.pbpoints.repository;

import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.User;
import com.pbpoints.domain.UserExtra;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserExtra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserExtraRepository extends JpaRepository<UserExtra, Long>, JpaSpecificationExecutor<UserExtra> {
    public Optional<UserExtra> findByUser(User user);

    public Optional<UserExtra> findOneByNumDoc(String NumDoc);
}
