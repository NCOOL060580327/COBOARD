package com.example.demo.member.performance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.member.batch.BlackListBatchScheduler;
import com.example.demo.member.entity.RefreshTokenBlackList;
import com.example.demo.member.entity.repository.RefreshTokenBlackListRepository;

@SpringBootTest
public class ChunkVsTaskletPerformanceTest {

  @PersistenceContext private EntityManager entityManager;

  @Autowired private BlackListBatchScheduler blackListBatchScheduler;

  @Autowired private RefreshTokenBlackListRepository refreshTokenBlackListRepository;

  private static final int TOKEN_COUNT = 100000;

  private void insertTestTokens() {
    List<RefreshTokenBlackList> tokens = new ArrayList<>();
    for (int i = 0; i < TOKEN_COUNT; i++) {
      tokens.add(RefreshTokenBlackList.of("token_" + i, LocalDateTime.now().minusDays(1)));
    }
    refreshTokenBlackListRepository.saveAll(tokens);
    System.out.printf("%d개의 만료된 토큰 추가 완료! %n", TOKEN_COUNT);
  }

  @Test
  @DisplayName("Tasklet 성능 테스트")
  void testQueryCountTasklet() throws JobExecutionException {
    Statistics stats =
        entityManager.unwrap(org.hibernate.Session.class).getSessionFactory().getStatistics();

    insertTestTokens();

    stats.clear();
    stats.setStatisticsEnabled(true);

    long start = System.nanoTime();
    blackListBatchScheduler.runBlacklistCleanupTaskletJob();
    long end = System.nanoTime();

    System.out.println("Tasklet 실행 시 Query Count: " + stats.getQueryExecutionCount());
    System.out.println("Tasklet 실행 시 트랜잭션 수: " + stats.getTransactionCount());
    System.out.println("Tasklet 실행 소요 시간: " + (end - start) / 1_000_000 + " ms");
    System.out.println("Tasklet 방식 최대 쿼리 실행 시간: " + stats.getQueryExecutionMaxTime() + " ms");
  }

  @Test
  @DisplayName("Chunk 성능 테스트")
  void testQueryCountChunk() throws JobExecutionException {
    Statistics stats =
        entityManager.unwrap(org.hibernate.Session.class).getSessionFactory().getStatistics();

    insertTestTokens();

    stats.clear();
    stats.setStatisticsEnabled(true);

    long start = System.nanoTime();
    blackListBatchScheduler.runBlacklistCleanupChunkJob();
    long end = System.nanoTime();

    System.out.println("Chunk 실행 시 Query Count: " + stats.getQueryExecutionCount());
    System.out.println("Chunk 실행 시 트랜잭션 수: " + stats.getTransactionCount());
    System.out.println("Chunk 실행 소요 시간: " + (end - start) / 1_000_000 + " ms");
    System.out.println("Chunk 방식 최대 쿼리 실행 시간: " + stats.getQueryExecutionMaxTime() + " ms");
  }
}
