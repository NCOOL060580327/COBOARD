package com.example.demo.post;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "post")
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

  @Setter
  @Column(name = "like_count", nullable = false)
  private Integer likeCount = 0;

  @Setter
  @Column(name = "average_rating")
  private Float averageRating;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
