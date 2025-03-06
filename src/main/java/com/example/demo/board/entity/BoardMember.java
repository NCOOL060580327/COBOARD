package com.example.demo.board.entity;

import jakarta.persistence.*;

import com.example.demo.member.entity.Member;

import lombok.*;

@Entity
@Table(name = "board_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BoardMember {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_member_id")
  private Long id;

  @Column(name = "is_leader", nullable = false, columnDefinition = "TINYINT default 0")
  private Boolean isLeader;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private Board board;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;
}
