package com.example.demo.post;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "post_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_code_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  private CodeLanguage language;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;
}
