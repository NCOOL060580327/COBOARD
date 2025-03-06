package com.example.demo.member.performance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.member.entity.RefreshTokenBlackList;
import com.example.demo.member.entity.repository.RefreshTokenBlackListRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeletePerformanceTest {

  @Autowired private RefreshTokenBlackListRepository refreshTokenBlackListRepository;

  private static final int TOKEN_COUNT = 1000000;

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
    System.out.println(TOKEN_COUNT + "개의 만료된 토큰 추가 완료!");
  }

  @Test
  @DisplayName("JPQL DELETE 성능 테스트")
  void testJpqlDeletePerformance() {
    insertTestTokens();
    assertThat(refreshTokenBlackListRepository.count()).isEqualTo(TOKEN_COUNT);

    LocalDateTime now = LocalDateTime.now();

    long startJpql = System.nanoTime();
    int deletedJpql = refreshTokenBlackListRepository.deleteExpiredTokens(now);
    long endJpql = System.nanoTime();

    long remainingAfterJpql = refreshTokenBlackListRepository.count();
    System.out.println("JPQL DELETE 후 남은 토큰 개수: " + remainingAfterJpql);
    System.out.println("JPQL DELETE 소요 시간: " + (endJpql - startJpql) / 1_000_000 + " ms");

    assertThat(deletedJpql).isGreaterThan(0);
    assertThat(remainingAfterJpql).isEqualTo(0);
  }

  @Test
  @DisplayName("Native Query DELETE 성능 테스트")
  void testNativeQueryDeletePerformance() {
    insertTestTokens();
    assertThat(refreshTokenBlackListRepository.count()).isEqualTo(TOKEN_COUNT);

    LocalDateTime now = LocalDateTime.now();

    long startNative = System.nanoTime();
    int deletedNative = refreshTokenBlackListRepository.deleteExpiredTokensNativeQuery(now);
    long endNative = System.nanoTime();

    long remainingAfterNative = refreshTokenBlackListRepository.count();
    System.out.println("Native Query DELETE 후 남은 토큰 개수: " + remainingAfterNative);
    System.out.println(
        "Native Query DELETE 소요 시간: " + (endNative - startNative) / 1_000_000 + " ms");

    assertThat(deletedNative).isGreaterThan(0);
    assertThat(remainingAfterNative).isEqualTo(0);
  }
}
