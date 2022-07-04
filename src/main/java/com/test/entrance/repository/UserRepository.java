package com.test.entrance.repository;

import com.test.entrance.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUsersByEmail(String email);
    Optional<User> findByEmail(String email);
}
