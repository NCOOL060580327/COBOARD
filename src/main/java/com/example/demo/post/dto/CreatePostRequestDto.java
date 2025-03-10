package com.example.demo.post.dto;

import com.example.demo.post.entity.CodeLanguage;

import lombok.Builder;

@Builder
public record CreatePostRequestDto(String title, CodeLanguage language, String content) {}
