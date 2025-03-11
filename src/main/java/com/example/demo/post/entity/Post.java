package com.example.demo.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;

import lombok.*;

@Entity
@Table(
    name = "post",
    indexes = {
      @Index(name = "idx_post_board_id", columnList = "board_id"),
      @Index(name = "idx_post_created_at_post_id", columnList = "board_id, post_id, created_at"),
      @Index(name = "idx_post_like_count", columnList = "board_id, like_count"),
      @Index(name = "idx_post_average_rating", columnList = "board_id, average_rating"),
      @Index(name = "idx_post_board_member_id", columnList = "board_member_id")
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;

  private String title;

  @Setter @Embedded private PostCode postCode;

  @Setter
  @Column(name = "like_count", nullable = false)
  @Builder.Default
  private Integer likeCount = 0;

  @Setter
  @Column(name = "average_rating")
  @Builder.Default
  private Float averageRating = 0F;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private Board board;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_member_id")
  private BoardMember boardMember;
}
