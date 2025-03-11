package com.example.demo.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.example.demo.board.entity.BoardMember;

import lombok.*;

@Entity
@Table(
    name = "post_like",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_post_like_post_board_member",
          columnNames = {"post_id", "board_member_id"})
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_like_id")
  private Long id;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_member_id")
  private BoardMember boardMember;
}
