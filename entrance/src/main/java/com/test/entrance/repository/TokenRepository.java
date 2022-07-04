package com.test.entrance.repository;

import com.test.entrance.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByUserId(long userId);
    Optional<Token> findByRefreshToken(String refreshToken);
}
