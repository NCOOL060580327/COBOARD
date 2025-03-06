package com.example.demo.board.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "thumbnail_image")
  private String thumbnailImage = "default-thumbnail.png";
}
