package com.example.demo.member.entity.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.member.entity.RefreshTokenBlackList;

public interface RefreshTokenBlackListRepository
    extends JpaRepository<RefreshTokenBlackList, Long> {

  boolean existsByRefreshToken(String refreshToken);

  @Modifying
  @Transactional
  @Query("""
    DELETE
    FROM RefreshTokenBlackList t
    WHERE t.expiredAt <= :now
    """)
  int deleteExpiredTokens(@Param("now") LocalDateTime now);
}
