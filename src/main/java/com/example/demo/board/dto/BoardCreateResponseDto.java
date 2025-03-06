package com.example.demo.board.dto;

import com.example.demo.board.entity.Board;

public record BoardCreateResponseDto(Long boardId) {
  public static BoardCreateResponseDto fromBoard(Board board) {
    return new BoardCreateResponseDto(board.getId());
  }
}
