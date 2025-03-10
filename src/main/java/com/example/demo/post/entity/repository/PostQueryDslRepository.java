package com.example.demo.post.entity.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.demo.board.entity.QBoardMember;
import com.example.demo.member.entity.QMember;
import com.example.demo.post.dto.GetPostListInBoardResponseDto;
import com.example.demo.post.dto.QGetPostListInBoardResponseDto;
import com.example.demo.post.entity.QPost;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostQueryDslRepository {

  private final JPAQueryFactory queryFactory;
  private static final QPost post = QPost.post;
  private static final QBoardMember boardMember = QBoardMember.boardMember;
  private static final QMember member = QMember.member;

  public Page<GetPostListInBoardResponseDto> findPostListByBoard(Long boardId, Pageable pageable) {
    List<GetPostListInBoardResponseDto> content =
        queryFactory
            .select(
                new QGetPostListInBoardResponseDto(
                    post.title,
                    post.likeCount,
                    post.averageRating,
                    post.createdAt,
                    member.nickname))
            .from(post)
            .leftJoin(post.boardMember, boardMember)
            .leftJoin(boardMember.member, member)
            .where(post.board.id.eq(boardId))
            .orderBy(getOrderSpecifiers(pageable))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    Long total =
        queryFactory.select(post.count()).from(post).where(post.board.id.eq(boardId)).fetchOne();

    return new PageImpl<>(content, pageable, total != null ? total : 0L);
  }

  private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
    return pageable.getSort().stream()
        .map(
            order ->
                switch (order.getProperty()) {
                  case "likeCount" -> post.likeCount.desc();
                  case "averageRating" -> post.averageRating.desc();
                  default -> new OrderSpecifier[] {post.createdAt.desc(), post.id.desc()};
                })
        .flatMap(
            spec ->
                spec instanceof OrderSpecifier<?>[]
                    ? Stream.of((OrderSpecifier<?>[]) spec)
                    : Stream.of((OrderSpecifier<?>) spec))
        .toArray(OrderSpecifier[]::new);
  }
}
