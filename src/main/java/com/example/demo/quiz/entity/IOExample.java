package com.example.demo.quiz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.*;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class IOExample {

  @Column(name = "input_example", columnDefinition = "TEXT")
  private String input;

  @Column(name = "output_example", columnDefinition = "TEXT")
  private String output;
}
