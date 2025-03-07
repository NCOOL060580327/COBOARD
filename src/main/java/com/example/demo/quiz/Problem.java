package com.example.demo.quiz;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "problem")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Problem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "problem_id")
  private Long id;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  private String source;

  @Column(name = "problem_link")
  private String problemLink;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "input_output_examples", joinColumns = @JoinColumn(name = "problem_id"))
  @Column(name = "example")
  private List<IOExample> examples = new ArrayList<>();
}
