package com.example.demo.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.board.entity.BoardMember;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.PostException;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostLike;
import com.example.demo.post.entity.repository.PostLikeJpaRepository;
import com.example.demo.post.service.PostLikeQueryService;

@ExtendWith(MockitoExtension.class)
public class PostLikeQueryServiceTest {

  @Mock private PostLikeJpaRepository postLikeJpaRepository;

  @InjectMocks private PostLikeQueryService postLikeQueryService;

  @Nested
  @DisplayName("게시글 좋아요를 조회할 때")
  class getPostLike {
    @Test
    @DisplayName("게시글 좋아요를 반환합니다.")
    void getPostLike_Success() {
      // give
      Post mockPost = mock(Post.class);
      BoardMember mockBoardMember = mock(BoardMember.class);

      PostLike testPostLike =
          PostLike.builder().post(mockPost).boardMember(mockBoardMember).build();

      when(postLikeJpaRepository.findPostLikeByPostAndBoardMember(mockPost, mockBoardMember))
          .thenReturn(Optional.of(testPostLike));

      // when
      PostLike result = postLikeQueryService.getPostLike(mockPost, mockBoardMember);

      // then
      assertNotNull(result, "조회 결과가 null이면 안됩니다.");
      assertEquals(mockPost, result.getPost(), "게시글이 일치해야합니다.");
      assertEquals(mockBoardMember, result.getBoardMember(), "게시판 회원이 일치해야합니다.");
      verify(postLikeJpaRepository, times(1))
          .findPostLikeByPostAndBoardMember(mockPost, mockBoardMember);
    }

    @Test
    @DisplayName("게시글 좋아요가 존재하지 않으면 예외를 발생시킵니다.")
    void getPostLike_Fail_Post_Like_Not_Found() {
      // give
      Post mockPost = mock(Post.class);
      BoardMember mockBoardMember = mock(BoardMember.class);

      PostLike testPostLike =
          PostLike.builder().post(mockPost).boardMember(mockBoardMember).build();

      when(postLikeJpaRepository.findPostLikeByPostAndBoardMember(mockPost, mockBoardMember))
          .thenReturn(Optional.empty());

      // when
      PostException result =
          assertThrows(
              PostException.class,
              () -> postLikeQueryService.getPostLike(mockPost, mockBoardMember));

      // then
      assertEquals(GlobalErrorCode.POST_LIKE_NOT_FOUND, result.getErrorCode(), "예외코드가 일치해야합니다.");
      verify(postLikeJpaRepository, times(1))
          .findPostLikeByPostAndBoardMember(mockPost, mockBoardMember);
    }
  }

  @Nested
  @DisplayName("게시글 좋아요가 존재하는지 조회할 때")
  class existsPostLike {
    @Test
    @DisplayName("존재하면 true를 반환합니다.")
    void existsPostLike_Return_True() {
      // give
      Post mockPost = mock(Post.class);
      BoardMember mockBoardMember = mock(BoardMember.class);

      when(postLikeJpaRepository.existsByPostAndBoardMember(mockPost, mockBoardMember))
          .thenReturn(true);

      // when
      boolean result = postLikeQueryService.existsPostLike(mockPost, mockBoardMember);

      // then
      assertTrue(result, "존재하면 true를 반환해야합니다.");
      verify(postLikeJpaRepository, times(1)).existsByPostAndBoardMember(mockPost, mockBoardMember);
    }

    @Test
    @DisplayName("존재하지 않으면 false를 반환합니다.")
    void existsPostLike_Return_False() {
      // give
      Post mockPost = mock(Post.class);
      BoardMember mockBoardMember = mock(BoardMember.class);

      when(postLikeJpaRepository.existsByPostAndBoardMember(mockPost, mockBoardMember))
          .thenReturn(false);

      // when
      Boolean result = postLikeQueryService.existsPostLike(mockPost, mockBoardMember);

      // then
      assertFalse(result, "존재하지 않으면 false를 반환해야합니다.");
      verify(postLikeJpaRepository, times(1)).existsByPostAndBoardMember(mockPost, mockBoardMember);
    }
  }
}
