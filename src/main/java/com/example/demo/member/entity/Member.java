package com.example.demo.member.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  @Embedded
  private Password password;

  @Column(nullable = false)
  private String nickname;

  @Column(name = "identified_code", unique = true, nullable = false)
  private String identifiedCode;

  @Column(name = "profile_image")
  private String profileImage = "default-profile.png";

  @Enumerated(EnumType.STRING)
  private Tier tier;

  @Column(name = "refresh_token")
  @Setter
  private String refreshToken;

  @Enumerated(EnumType.STRING)
  @Column(name = "member_role")
  private MemberRole memberRole;
}
