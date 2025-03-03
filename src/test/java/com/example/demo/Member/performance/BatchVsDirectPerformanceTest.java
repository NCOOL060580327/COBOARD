package com.example.demo.Member.performance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.member.batch.BlackListBatchScheduler;
import com.example.demo.member.entity.RefreshTokenBlackList;
import com.example.demo.member.entity.repository.RefreshTokenBlackListRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BatchVsDirectPerformanceTest {

  @Autowired private BlackListBatchScheduler batchScheduler;

  @Autowired private RefreshTokenBlackListRepository refreshTokenBlackListRepository;

  @BeforeEach
  void setUp() {
    refreshTokenBlackListRepository.deleteAll();
  }

  @Test
  @DisplayName("배치로 만료 토큰 삭제 성능 테스트")
  void testBatchDelete() {
    for (int i = 0; i < 100000; i++) {
      refreshTokenBlackListRepository.save(
          RefreshTokenBlackList.of("token_" + i, LocalDateTime.now().minusDays(1)));
    }
    long startTime = System.currentTimeMillis();
    batchScheduler.runBlacklistCleanupJob();
    long endTime = System.currentTimeMillis();
    int remaining = refreshTokenBlackListRepository.findAll().size();
    System.out.println("배치 실행 후 남은 토큰 개수: " + remaining);
    System.out.println("배치 실행 소요 시간: " + (endTime - startTime) + " ms");
    assertEquals(0, remaining, "배치 실행 후 만료된 토큰이 모두 삭제되어야 합니다.");
  }

  @Test
  @DisplayName("직접 삭제로 만료 토큰 삭제 성능 테스트")
  void testDirectDelete() {
    for (int i = 0; i < 100000; i++) {
      refreshTokenBlackListRepository.save(
          RefreshTokenBlackList.of("token_" + i, LocalDateTime.now().minusDays(1)));
    }
    long startTime = System.currentTimeMillis();
    int deletedCount = refreshTokenBlackListRepository.deleteExpiredTokens(LocalDateTime.now());
    long endTime = System.currentTimeMillis();
    int remaining = refreshTokenBlackListRepository.findAll().size();
    System.out.println("직접 삭제 후 남은 토큰 개수: " + remaining);
    System.out.println("직접 삭제 소요 시간: " + (endTime - startTime) + " ms");
    assertEquals(0, remaining, "직접 삭제 후 만료된 토큰이 모두 삭제되어야 합니다.");
  }
}
