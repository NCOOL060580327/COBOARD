package com.example.demo.post.entity;

import jakarta.persistence.*;

import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostCode {

  @Enumerated(EnumType.STRING)
  private CodeLanguage language;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;
}
