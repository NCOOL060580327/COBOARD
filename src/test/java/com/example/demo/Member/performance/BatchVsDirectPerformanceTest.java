package com.example.demo.Member.performance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.example.demo.member.batch.BlackListBatchScheduler;
import com.example.demo.member.entity.RefreshTokenBlackList;
import com.example.demo.member.entity.repository.RefreshTokenBlackListRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BatchVsDirectPerformanceTest {

  @Autowired private EntityManagerFactory entityManagerFactory;

  @Autowired private BlackListBatchScheduler batchScheduler;

  @Autowired private RefreshTokenBlackListRepository refreshTokenBlackListRepository;

  @SpyBean private RefreshTokenBlackListRepository mockTokenRepository;

  private static final int TOKEN_COUNT = 100000;

  @BeforeEach
  void setUp() {
    refreshTokenBlackListRepository.deleteAll();
  }

  private void insertTestTokens() {
    List<RefreshTokenBlackList> tokens = new ArrayList<>();
    for (int i = 0; i < TOKEN_COUNT; i++) {
      tokens.add(RefreshTokenBlackList.of("token_" + i, LocalDateTime.now().minusDays(1)));
    }
    refreshTokenBlackListRepository.saveAll(tokens);
    System.out.printf("%d개의 만료된 토큰 추가 완료! %n", TOKEN_COUNT);
  }

  @Test
  @DisplayName("Tasklet 기반 배치로 만료 토큰 삭제 성능 테스트")
  void testBatchDelete() throws JobExecutionException {

    insertTestTokens();

    long startTime = System.nanoTime();
    batchScheduler.runBlacklistCleanupTaskletJob();
    long endTime = System.nanoTime();
    int remaining = refreshTokenBlackListRepository.findAll().size();
    System.out.println("배치 실행 후 남은 토큰 개수: " + remaining);
    System.out.println("배치 실행 소요 시간: " + (endTime - startTime) / 1_000_000 + " ms");
    assertEquals(0, remaining, "배치 실행 후 만료된 토큰이 모두 삭제되어야 합니다.");
  }

  @Test
  @DisplayName("직접 삭제로 만료 토큰 삭제 성능 테스트")
  void testDirectDelete() {

    insertTestTokens();

    long startTime = System.nanoTime();
    refreshTokenBlackListRepository.deleteExpiredTokens(LocalDateTime.now());
    long endTime = System.nanoTime();
    int remaining = refreshTokenBlackListRepository.findAll().size();
    System.out.println("직접 삭제 후 남은 토큰 개수: " + remaining);
    System.out.println("직접 삭제 소요 시간: " + (endTime - startTime) / 1_000_000 + " ms");
    assertEquals(0, remaining, "직접 삭제 후 만료된 토큰이 모두 삭제되어야 합니다.");
  }

  @Test
  @DisplayName("직접 DELETE 실행 중 장애 발생 시 복구 불가 테스트")
  void testDirectDeleteFailure() {
    insertTestTokens();
    long initialCount = refreshTokenBlackListRepository.count();

    assertThrows(
        RuntimeException.class,
        () -> {
          EntityManager em = entityManagerFactory.createEntityManager();
          EntityTransaction tx = em.getTransaction();
          try {
            tx.begin();
            em.createQuery("DELETE FROM RefreshTokenBlackList t").executeUpdate();
            tx.commit();
            throw new RuntimeException("DELETE 실행 중 장애 발생!");
          } finally {
            em.close();
          }
        });

    long remaining = refreshTokenBlackListRepository.count();
    System.out.println("직접 DELETE 실행 후 남은 토큰 개수: " + remaining);
    assertThat(initialCount).isEqualTo(TOKEN_COUNT);
    assertThat(remaining).isNotEqualTo(initialCount);
  }

  @Test
  @DisplayName("Tasklet 기반 배치 실행 중 장애 발생 시 롤백 가능 테스트")
  void testTaskletBatchFailure() {
    insertTestTokens();
    long initialCount = refreshTokenBlackListRepository.count();
    assertThat(initialCount).isEqualTo(TOKEN_COUNT);

    doThrow(new RuntimeException("Tasklet 실행 중 장애 발생!"))
        .when(mockTokenRepository)
        .deleteExpiredTokens(any());

    assertThrows(JobExecutionException.class, () -> batchScheduler.runBlacklistCleanupTaskletJob());

    long remaining = refreshTokenBlackListRepository.count();
    System.out.println("Tasklet 배치 실행 후 남은 토큰 개수: " + remaining);
    assertThat(remaining).isEqualTo(initialCount);
  }
}
