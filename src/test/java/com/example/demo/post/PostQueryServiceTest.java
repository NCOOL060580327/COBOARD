package com.example.demo.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

import com.example.demo.post.dto.GetPostListInBoardResponseDto;
import com.example.demo.post.entity.repository.PostQueryDslRepository;
import com.example.demo.post.service.PostQueryService;

@ExtendWith(MockitoExtension.class)
public class PostQueryServiceTest {

  @Mock private PostQueryDslRepository postQueryDslRepository;

  @InjectMocks private PostQueryService postQueryService;

  private final Long testId = Long.parseLong(PostTestConst.TEST_ID.getValue());
  private final String testTitle = PostTestConst.TEST_TITLE.getValue();
  private final Integer testLikeCount = Integer.parseInt(PostTestConst.TEST_COUNT.getValue());
  private final Float testAverageRating = Float.parseFloat(PostTestConst.TEST_COUNT.getValue());
  private final String testNickname = PostTestConst.TEST_TITLE.getValue();

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
}
