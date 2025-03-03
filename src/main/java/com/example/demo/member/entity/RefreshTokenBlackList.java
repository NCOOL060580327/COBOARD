package com.example.demo.member.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token_black_list")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenBlackList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "refresh_token", nullable = false, unique = true)
  private String refreshToken;

  @Column(name = "expired_at", nullable = false)
  private LocalDateTime expiredAt;

  public static RefreshTokenBlackList of(String refreshToken, LocalDateTime expiredAt) {
    return RefreshTokenBlackList.builder().refreshToken(refreshToken).expiredAt(expiredAt).build();
  }
}
