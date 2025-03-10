package com.example.demo.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.board.entity.BoardMember;
import com.example.demo.post.dto.CreatePostRequestDto;
import com.example.demo.post.entity.CodeLanguage;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostCode;
import com.example.demo.post.entity.repository.PostJpaRepository;
import com.example.demo.post.service.PostCommandService;

@ExtendWith(MockitoExtension.class)
public class PostCommandServiceTest {

  @Mock private PostJpaRepository postJpaRepository;

  @InjectMocks private PostCommandService postCommandService;

  private final String testTitle = PostTestConst.TEST_TITLE.getValue();
  private final String testContent = PostTestConst.TEST_CONTENT.getValue();

  @BeforeEach
  void setUp() {}

  @Nested
  @DisplayName("게시글을 생성할 때")
  class createPost {
    @Test
    @DisplayName("생성 요청 데이터를 기반으로 새로운 게시글을 생성합니다.")
    void createPost_Success() {
      // give
      CreatePostRequestDto requestDto =
          CreatePostRequestDto.builder()
              .title(testTitle)
              .language(CodeLanguage.C)
              .content(testContent)
              .build();

      PostCode testPostCode =
          PostCode.builder().language(CodeLanguage.C).content(requestDto.content()).build();

      BoardMember mockBoardMember = mock(BoardMember.class);

      Post testPost =
          Post.builder()
              .title(requestDto.title())
              .postCode(testPostCode)
              .boardMember(mockBoardMember)
              .build();

      when(postJpaRepository.save(any(Post.class))).thenReturn(testPost);

      // when
      postCommandService.createPost(requestDto, mockBoardMember);

      // then
      ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
      verify(postJpaRepository, times(1)).save(captor.capture());

      Post capturedPost = captor.getValue();
      assertEquals(requestDto.title(), capturedPost.getTitle(), "게시글 제목이 일치해야합니다.");
      assertEquals(
          requestDto.language(), capturedPost.getPostCode().getLanguage(), "게시글 코드 언어가 일치해야합니다.");
      assertEquals(
          requestDto.content(), capturedPost.getPostCode().getContent(), "게시글의 코드가 일치해야합니다.");
      assertEquals(mockBoardMember, capturedPost.getBoardMember(), "작성자와 게시글 회원이 일치해야합니다.");
    }
  }
}
