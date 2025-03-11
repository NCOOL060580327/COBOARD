package com.example.demo.post.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

public record GetPostListInBoardResponseDto(
    String title,
    Integer likeCount,
    Float averageRating,
    LocalDateTime createdAt,
    String nickname) {

  @QueryProjection
  public GetPostListInBoardResponseDto(
      String title,
      Integer likeCount,
      Float averageRating,
      LocalDateTime createdAt,
      String nickname) {
    this.title = title;
    this.likeCount = likeCount;
    this.averageRating = averageRating;
    this.createdAt = createdAt;
    this.nickname = nickname;
  }
}
