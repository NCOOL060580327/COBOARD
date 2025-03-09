package com.example.demo.quiz.dto;

import java.util.List;

import com.example.demo.quiz.entity.IOExample;

import lombok.Builder;

@Builder
public record CreateProblemRequestDto(
    String title,
    String description,
    String source,
    String problemLink,
    List<IOExample> ioExampleList) {}
