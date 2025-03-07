package com.example.demo.board.entity.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.board.dto.response.GetMyBoardListResponseDto;
import com.example.demo.board.entity.QBoard;
import com.example.demo.board.entity.QBoardMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class BoardQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  public List<GetMyBoardListResponseDto> findBoardListByMemberId(
      Long memberId, Long lastBoardId, int pageSize) {
    QBoard board = QBoard.board;
    QBoardMember boardMember = QBoardMember.boardMember;

    return queryFactory
        .select(
            Projections.constructor(
                GetMyBoardListResponseDto.class, board.id, board.name, board.thumbnailImage))
        .from(boardMember)
        .join(boardMember.board, board)
        .where(
            boardMember.member.id.eq(memberId),
            lastBoardId == null ? null : board.id.lt(lastBoardId))
        .orderBy(board.id.desc())
        .limit(pageSize)
        .fetch();
  }
}
