package com.example.demo.board.dto.response;

import com.example.demo.board.entity.Board;

import lombok.Builder;

@Builder
public record GetMyBoardListResponseDto(Long id, String name, String thumbnailImage) {
  public static GetMyBoardListResponseDto fromBoard(Board board) {
    return GetMyBoardListResponseDto.builder()
        .id(board.getId())
        .name(board.getName())
        .thumbnailImage(board.getThumbnailImage())
        .build();
  }
}
