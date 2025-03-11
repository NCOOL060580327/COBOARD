package com.example.demo.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.PostException;
import com.example.demo.post.dto.GetPostListInBoardResponseDto;
import com.example.demo.post.entity.CodeLanguage;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostCode;
import com.example.demo.post.entity.repository.PostJpaRepository;
import com.example.demo.post.entity.repository.PostQueryDslRepository;
import com.example.demo.post.service.PostQueryService;

@ExtendWith(MockitoExtension.class)
public class PostQueryServiceTest {

  @Mock private PostQueryDslRepository postQueryDslRepository;

  @Mock private PostJpaRepository postJpaRepository;

  @InjectMocks private PostQueryService postQueryService;

  private final Long testId = Long.parseLong(PostTestConst.TEST_ID.getValue());
  private final String testTitle = PostTestConst.TEST_TITLE.getValue();
  private final Integer testLikeCount = Integer.parseInt(PostTestConst.TEST_COUNT.getValue());
  private final Float testAverageRating = Float.parseFloat(PostTestConst.TEST_COUNT.getValue());
  private final String testNickname = PostTestConst.TEST_TITLE.getValue();
  private final String testContent = PostTestConst.TEST_CONTENT.getValue();

  @Nested
  @DisplayName("게시판의 게시글을 조회할 때")
  class getPostListInBoard {
    @Test
    @DisplayName("게시판의 글을 조회합니다.")
    void getPostListInBoard_Success() {
      // give
      GetPostListInBoardResponseDto responseDto =
          new GetPostListInBoardResponseDto(
              testTitle, testLikeCount, testAverageRating, LocalDateTime.now(), testNickname);

      Pageable testPageable = PageRequest.of(0, 10);

      List<GetPostListInBoardResponseDto> content = Collections.singletonList(responseDto);
      Page<GetPostListInBoardResponseDto> mockPage = new PageImpl<>(content, testPageable, 1);

      when(postQueryDslRepository.findPostListByBoard(testId, testPageable)).thenReturn(mockPage);

      // when
      Page<GetPostListInBoardResponseDto> result =
          postQueryService.getPostListInBoard(testId, testPageable);

      // then
      assertNotNull(result, "조회 결과가 null이면 안됩니다.");
      assertEquals(1, result.getTotalElements(), "총 개수는 1입니다.");
      assertEquals(1, result.getContent().size(), "content 크기는 1입니다.");
      assertEquals(testTitle, result.getContent().getFirst().title(), "제목이 일치해야합니다.");
      assertEquals(testNickname, result.getContent().getFirst().nickname(), "닉네임이 일치해야합니다.");

      verify(postQueryDslRepository, times(1)).findPostListByBoard(testId, testPageable);
    }
  }

  @Nested
  @DisplayName("게시글 ID를 통해 게시글을 조회할 떄")
  class getPostById {
    @Test
    @DisplayName("게시글을 조회합니다.")
    void getPostById_Success() {
      // give
      PostCode testPostCode =
          PostCode.builder().language(CodeLanguage.C).content(testContent).build();

      Board mockBoard = mock(Board.class);

      BoardMember mockBoardMember = mock(BoardMember.class);

      Post testPost =
          Post.builder()
              .title(testTitle)
              .postCode(testPostCode)
              .board(mockBoard)
              .boardMember(mockBoardMember)
              .build();

      when(postJpaRepository.findById(testId)).thenReturn(Optional.of(testPost));

      // when
      Post result = postQueryService.getPostById(testId);

      // then
      assertNotNull(result, "조회결과가 null이면 안됩니다.");
      assertEquals(testTitle, result.getTitle(), "게시글 제목이 일치해야합니다.");
      verify(postJpaRepository, times(1)).findById(testId);
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 예외를 발생시킵니다.")
    void getPostById_Fail_Post_Not_Found() {
      // give
      when(postJpaRepository.findById(testId)).thenReturn(Optional.empty());

      // when
      PostException exception =
          assertThrows(PostException.class, () -> postQueryService.getPostById(testId));

      // then
      assertEquals(GlobalErrorCode.POST_NOT_FOUND, exception.getErrorCode(), "예외코드가 일치해야합니다.");
      verify(postJpaRepository, times(1)).findById(testId);
    }
  }
}
