package com.example.demo.post.performance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.board.entity.repository.BoardMemberRepository;
import com.example.demo.board.entity.repository.BoardRepository;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.MemberRole;
import com.example.demo.member.entity.Password;
import com.example.demo.member.entity.Tier;
import com.example.demo.member.entity.repository.MemberRepository;
import com.example.demo.post.entity.CodeLanguage;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostCode;
import com.example.demo.post.entity.repository.PostJpaRepository;
import com.example.demo.post.entity.repository.PostLikeJpaRepository;
import com.example.demo.post.facade.PostFacade;

@SpringBootTest
public class PostLikeSynchronicityTest {

  @Autowired private PostFacade postFacade;
  @Autowired private PostJpaRepository postJpaRepository;
  @Autowired private PostLikeJpaRepository postLikeJpaRepository;
  @Autowired private BoardRepository boardJpaRepository;
  @Autowired private BoardMemberRepository boardMemberJpaRepository;
  @Autowired private MemberRepository memberJpaRepository;

  private Board testBoard;
  private Member testMember;
  private BoardMember testBoardMember;
  private Post testPost;

  @BeforeEach
  void setUp() {
    testMember =
        memberJpaRepository.save(
            Member.builder()
                .email("testEmail")
                .password(new Password("Test1234!@#$"))
                .nickname("testNickname")
                .profileImage("testProfileImage")
                .identifiedCode("test")
                .memberRole(MemberRole.USER)
                .refreshToken("testRefreshToken")
                .tier(Tier.UNRANK)
                .build());

    testBoard =
        boardJpaRepository.save(
            Board.builder().name("testName").thumbnailImage("defaultThumbnailImage").build());

    testBoardMember =
        boardMemberJpaRepository.save(
            BoardMember.builder().board(testBoard).member(testMember).isLeader(true).build());

    PostCode testPostCode = PostCode.builder().language(CodeLanguage.C).content("content").build();

    testPost =
        postJpaRepository.save(
            Post.builder()
                .title("testTitle")
                .postCode(testPostCode)
                .board(testBoard)
                .boardMember(testBoardMember)
                .build());
  }

  @Test
  @DisplayName("동시에 여러 스레드가 좋아요를 실행하면 likeCount와 PostLike 수가 일치한다.")
  void ConcurrentCreatePostLike() throws InterruptedException {
    int threadCount = 10; // 10개 스레드로 테스트
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      Member member =
          memberJpaRepository.save(
              Member.builder()
                  .email("testEmail" + i)
                  .password(new Password("Test1234!@#$"))
                  .nickname("testNickname")
                  .profileImage("testProfileImage")
                  .identifiedCode("test" + i)
                  .memberRole(MemberRole.USER)
                  .refreshToken("testRefreshToken")
                  .tier(Tier.UNRANK)
                  .build());

      BoardMember boardMember =
          boardMemberJpaRepository.save(
              BoardMember.builder().board(testBoard).member(member).isLeader(true).build());

      executorService.submit(
          () -> {
            try {
              postFacade.createPostLike(testBoard.getId(), member, testPost.getId());
            } catch (Exception e) {
              System.out.println("Thread failed to create post like" + e);
            } finally {
              latch.countDown();
            }
          });
    }

    // 모든 스레드 작업 완료 대기
    latch.await(5, TimeUnit.SECONDS);
    executorService.shutdown();

    // 결과 검증
    Post updatedPost = postJpaRepository.findById(testPost.getId()).orElseThrow();

    assertEquals(threadCount, updatedPost.getLikeCount(), "likeCount가 스레드 수와 일치해야 함");
  }
}
