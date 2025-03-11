package com.example.demo.post.performance;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.MemberRole;
import com.example.demo.member.entity.Password;
import com.example.demo.member.entity.Tier;
import com.example.demo.post.dto.GetPostListInBoardResponseDto;
import com.example.demo.post.entity.CodeLanguage;
import com.example.demo.post.entity.PostCode;
import com.example.demo.post.service.PostQueryService;

@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class IndexTest {

  @Autowired private PostQueryService postQueryService;

  @Autowired private JdbcTemplate jdbcTemplate;

  @PersistenceContext EntityManager entityManager;

  @Autowired TransactionTemplate transactionTemplate;

  private static final int BULK_INSERT_SIZE = 1000;
  private static final int EXECUTE_COUNT = 2000;
  private static final int TOTAL_POSTS = BULK_INSERT_SIZE * EXECUTE_COUNT;
  private static final int REPEAT_COUNT = 10;

  private Member testMember;
  private Board testBoard;
  private BoardMember testBoardMember;
  private PostCode testPostCode;

  @BeforeEach
  void setupTestData() throws InterruptedException {
    setupInitialData();

    insertData();
  }

  private void setupInitialData() {
    transactionTemplate.executeWithoutResult(
        status -> {
          testMember =
              Member.builder()
                  .email("testEmail")
                  .password(new Password("testPassword"))
                  .nickname("testNickname")
                  .profileImage("testProfileImage")
                  .identifiedCode("test")
                  .memberRole(MemberRole.USER)
                  .tier(Tier.UNRANK)
                  .build();
          entityManager.persist(testMember);

          testBoard = Board.builder().name("testName").thumbnailImage("testThumbnailImage").build();
          entityManager.persist(testBoard);

          testBoardMember =
              BoardMember.builder().board(testBoard).member(testMember).isLeader(true).build();
          entityManager.persist(testBoardMember);

          testPostCode =
              PostCode.builder().language(CodeLanguage.JAVA).content("testContent").build();
          entityManager.flush();
        });
  }

  @AfterEach
  void clearDatabase() {
    transactionTemplate.executeWithoutResult(
        status -> {
          entityManager.createNativeQuery("DELETE FROM post").executeUpdate();
          entityManager.createNativeQuery("DELETE FROM board_member").executeUpdate();
          entityManager.createNativeQuery("DELETE FROM board").executeUpdate();
          entityManager.createNativeQuery("DELETE FROM member").executeUpdate();
          entityManager.clear();
          entityManager.getEntityManagerFactory().getCache().evictAll();
        });

    System.out.println("DB 초기화 완료");
  }

  private void insertData() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    // 인덱스 일시 삭제
    transactionTemplate.executeWithoutResult(
        status -> {
          try {
            entityManager
                .createNativeQuery("DROP INDEX idx_post_created_at_post_id ON POST")
                .executeUpdate();
          } catch (Exception e) {
            System.out.println("Index drop failed: " + e.getMessage());
          }
        });

    long startTime = System.nanoTime();
    for (int i = 0; i < EXECUTE_COUNT; i++) {
      final int batchNumber = i;
      executorService.submit(
          () -> {
            try {
              transactionTemplate.executeWithoutResult(status -> insert(batchNumber));
            } catch (Exception e) {
              System.err.println("Insert failed for batch " + batchNumber + ": " + e.getMessage());
            } finally {
              latch.countDown();
              System.out.println("latch.getCount() = " + latch.getCount());
              if (batchNumber % 500 == 0) { // 500번째 배치마다 출력
                long elapsed = (System.nanoTime() - startTime) / 1_000_000;
                System.out.println(
                    "Batch "
                        + batchNumber
                        + ", latch.getCount() = "
                        + latch.getCount()
                        + ", Time elapsed: "
                        + elapsed
                        + "ms");
              }
            }
          });
    }

    latch.await();
    executorService.shutdown();
    executorService.awaitTermination(10, TimeUnit.MINUTES);

    Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM POST", Long.class);
    System.out.println(
        "Total inserted posts: "
            + count
            + ", Total time: "
            + (System.nanoTime() - startTime) / 1_000_000
            + "ms");

    // 인덱스 복구
    transactionTemplate.executeWithoutResult(
        status ->
            entityManager
                .createNativeQuery(
                    "CREATE INDEX idx_post_created_at_post_id ON POST (board_id, post_id DESC, created_at DESC)")
                .executeUpdate());
  }

  private void insert(int batchNumber) {
    jdbcTemplate.batchUpdate(
        "INSERT INTO POST (title, created_at, board_id, board_member_id, language, content, like_count, average_rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, "title_" + batchNumber + "_" + i);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(3, testBoard.getId());
            ps.setLong(4, testBoardMember.getId());
            ps.setString(5, testPostCode.getLanguage().name());
            ps.setString(6, testPostCode.getContent());
            ps.setInt(7, 0);
            ps.setFloat(8, 0.0f);
          }

          @Override
          public int getBatchSize() {
            return BULK_INSERT_SIZE;
          }
        });
  }

  @Nested
  @DisplayName("인덱스 성능 테스트")
  class GetPostInBoard {

    int pageSize = 20;
    int totalPosts = TOTAL_POSTS;
    int middlePage = totalPosts / (2 * pageSize);
    int lastPage = (totalPosts / pageSize) - 1;

    private List<Long> withIndexTimesStart = new ArrayList<>();
    private List<Long> withIndexTimesMiddle = new ArrayList<>();
    private List<Long> withIndexTimesEnd = new ArrayList<>();
    private List<Long> withoutIndexTimesStart = new ArrayList<>();
    private List<Long> withoutIndexTimesMiddle = new ArrayList<>();
    private List<Long> withoutIndexTimesEnd = new ArrayList<>();

    @RepeatedTest(
        value = REPEAT_COUNT,
        name = "{displayName} - 반복 {currentRepetition}/{totalRepetitions}")
    @DisplayName("인덱스가 있는 경우")
    void getPostInBoard_With_Index() {

      long startResultStartTime = System.nanoTime();

      Page<GetPostListInBoardResponseDto> startResult =
          postQueryService.getPostListInBoard(testBoard.getId(), PageRequest.of(0, pageSize));

      long startResultEndTime = System.nanoTime();

      long startDuration = (startResultEndTime - startResultStartTime) / 1_000_000;
      System.out.println("인덱스가 있는 경우 첫 페이지 실행 시간: " + startDuration + "ms");
      withIndexTimesStart.add(startDuration);

      long middleResultStartTime = System.nanoTime();

      Page<GetPostListInBoardResponseDto> middleResult =
          postQueryService.getPostListInBoard(
              testBoard.getId(), PageRequest.of(middlePage, pageSize));

      long middleResultEndTime = System.nanoTime();

      long middleDuration = (middleResultEndTime - middleResultStartTime) / 1_000_000;
      System.out.println("인덱스가 있는 경우 중간 페이지 실행 시간: " + middleDuration + "ms");
      withIndexTimesMiddle.add(middleDuration);

      long lastResultStartTime = System.nanoTime();

      Page<GetPostListInBoardResponseDto> lastResult =
          postQueryService.getPostListInBoard(
              testBoard.getId(), PageRequest.of(lastPage, pageSize));

      long lastResultEndTime = System.nanoTime();

      long lastDuration = (lastResultEndTime - lastResultStartTime) / 1_000_000;
      System.out.println("인덱스가 있는 경우 마지막 페이지 실행 시간: " + lastDuration + "ms");
      withIndexTimesEnd.add(lastDuration);

      Assertions.assertFalse(startResult.getContent().isEmpty());
      Assertions.assertEquals(TOTAL_POSTS, startResult.getTotalElements());
      Assertions.assertFalse(middleResult.getContent().isEmpty());
      Assertions.assertEquals(TOTAL_POSTS, middleResult.getTotalElements());
      Assertions.assertFalse(lastResult.getContent().isEmpty());
      Assertions.assertEquals(TOTAL_POSTS, lastResult.getTotalElements());

      double startAverage =
          withIndexTimesStart.stream().mapToLong(Long::longValue).average().orElse(0);
      double middleAverage =
          withIndexTimesMiddle.stream().mapToLong(Long::longValue).average().orElse(0);
      double lastAverage =
          withIndexTimesEnd.stream().mapToLong(Long::longValue).average().orElse(0);

      System.out.println("인덱스가 있는 경우 시작 평균 실행 시간: " + startAverage + "ms");
      System.out.println("인덱스가 있는 경우 중간 평균 실행 시간: " + middleAverage + "ms");
      System.out.println("인덱스가 있는 경우 마지막 평균 실행 시간: " + lastAverage + "ms");
    }

    @RepeatedTest(
        value = REPEAT_COUNT,
        name = "{displayName} - 반복 {currentRepetition}/{totalRepetitions}")
    @DisplayName("인덱스가 없는 경우")
    void getPostInBoard_Without_Index() {

      transactionTemplate.executeWithoutResult(
          status -> {
            try {
              entityManager
                  .createNativeQuery("DROP INDEX idx_post_created_at_post_id ON post")
                  .executeUpdate();
              System.out.println("인덱스 삭제 성공");
            } catch (Exception e) {
              System.out.println("인덱스가 존재하지 않음, 삭제할 필요 없음");
            }
          });

      long startResultStartTime = System.nanoTime();

      Page<GetPostListInBoardResponseDto> startResult =
          postQueryService.getPostListInBoard(testBoard.getId(), PageRequest.of(0, pageSize));

      long startResultEndTime = System.nanoTime();

      long startDuration = (startResultEndTime - startResultStartTime) / 1_000_000;
      System.out.println("인덱스가 없는 경우 첫 페이지 실행 시간: " + startDuration + "ms");
      withoutIndexTimesStart.add(startDuration);

      long middleResultStartTime = System.nanoTime();

      Page<GetPostListInBoardResponseDto> middleResult =
          postQueryService.getPostListInBoard(
              testBoard.getId(), PageRequest.of(middlePage, pageSize));

      long middleResultEndTime = System.nanoTime();

      long middleDuration = (middleResultEndTime - middleResultStartTime) / 1_000_000;
      System.out.println("인덱스가 없는 경우 중간 페이지 실행 시간: " + middleDuration + "ms");
      withoutIndexTimesMiddle.add(middleDuration);

      long lastResultStartTime = System.nanoTime();

      Page<GetPostListInBoardResponseDto> lastResult =
          postQueryService.getPostListInBoard(
              testBoard.getId(), PageRequest.of(lastPage, pageSize));

      long lastResultEndTime = System.nanoTime();

      long lastDuration = (lastResultEndTime - lastResultStartTime) / 1_000_000;
      System.out.println("인덱스가 없는 경우 마지막 페이지 실행 시간: " + lastDuration + "ms");
      withoutIndexTimesEnd.add(lastDuration);

      Assertions.assertFalse(startResult.getContent().isEmpty());
      Assertions.assertEquals(TOTAL_POSTS, startResult.getTotalElements());
      Assertions.assertFalse(middleResult.getContent().isEmpty());
      Assertions.assertEquals(TOTAL_POSTS, middleResult.getTotalElements());
      Assertions.assertFalse(lastResult.getContent().isEmpty());
      Assertions.assertEquals(TOTAL_POSTS, lastResult.getTotalElements());

      transactionTemplate.executeWithoutResult(
          status ->
              entityManager
                  .createNativeQuery(
                      "CREATE INDEX idx_post_created_at_post_id ON POST (board_id, post_id DESC, created_at DESC)")
                  .executeUpdate());

      double startAverage =
          withoutIndexTimesStart.stream().mapToLong(Long::longValue).average().orElse(0);
      double middleAverage =
          withoutIndexTimesMiddle.stream().mapToLong(Long::longValue).average().orElse(0);
      double lastAverage =
          withoutIndexTimesEnd.stream().mapToLong(Long::longValue).average().orElse(0);

      System.out.println("인덱스가 없는 경우 시작 평균 실행 시간: " + startAverage + "ms");
      System.out.println("인덱스가 없는 경우 중간 평균 실행 시간: " + middleAverage + "ms");
      System.out.println("인덱스가 없는 경우 마지막 평균 실행 시간: " + lastAverage + "ms");
    }
  }
}
