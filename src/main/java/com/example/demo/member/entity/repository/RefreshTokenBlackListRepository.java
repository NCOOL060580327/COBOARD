package com.example.demo.member.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.member.entity.RefreshTokenBlackList;

public interface RefreshTokenBlackListRepository
    extends JpaRepository<RefreshTokenBlackList, Long> {

  boolean existsByRefreshToken(String refreshToken);
}
