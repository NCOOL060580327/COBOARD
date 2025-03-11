package com.example.demo.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.board.entity.BoardMember;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostLike;
import com.example.demo.post.entity.repository.PostLikeJpaRepository;
import com.example.demo.post.service.PostLikeCommandService;

@ExtendWith(MockitoExtension.class)
public class PostLikeCommandServiceTest {

  @Mock private PostLikeJpaRepository postLikeJpaRepository;

  @InjectMocks private PostLikeCommandService postLikeCommandService;

  @Nested
  @DisplayName("게시물 좋아요를 생성할 때")
  class createPostLike {
    @Test
    @DisplayName("게시물 좋아요를 생성하고 true를 반환합니다.")
    void createPostLike_Success() {
      // give
      Post mockPost = mock(Post.class);
      BoardMember mockBoardMember = mock(BoardMember.class);

      PostLike testPostLike =
          PostLike.builder().post(mockPost).boardMember(mockBoardMember).build();

      when(postLikeJpaRepository.save(any(PostLike.class))).thenReturn(testPostLike);

      // when
      Boolean result = postLikeCommandService.createPostLike(mockPost, mockBoardMember);

      // then
      ArgumentCaptor<PostLike> captor = ArgumentCaptor.forClass(PostLike.class);
      verify(postLikeJpaRepository, times(1)).save(captor.capture());

      PostLike postLike = captor.getValue();
      assertTrue(result, "true가 일치해야합니다.");
      assertEquals(mockPost, postLike.getPost(), "게시글이 일치해야합니다.");
      assertEquals(mockBoardMember, postLike.getBoardMember(), "게시판 회원이 일치해야합니다.");
    }
  }

  @Nested
  @DisplayName("게시글 좋아요를 삭제할 때")
  class deletePostLike {
    @Test
    @DisplayName("게시글 좋아요를 삭제하고 false를 반환합니다.")
    void deletePostLike_Success() {
      // give
      Post mockPost = mock(Post.class);
      BoardMember mockBoardMember = mock(BoardMember.class);

      PostLike testPostLike =
          PostLike.builder().post(mockPost).boardMember(mockBoardMember).build();

      // when
      Boolean result = postLikeCommandService.deletePostLike(testPostLike);

      // then
      assertFalse(result, "false가 일치해야합니다.");
      verify(postLikeJpaRepository, times(1)).delete(testPostLike);
    }
  }
}
