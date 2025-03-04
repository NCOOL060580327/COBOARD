package com.example.demo.member.batch;

import java.time.LocalDateTime;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.example.demo.member.entity.repository.RefreshTokenBlackListRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BlackListCleanUpTasklet implements Tasklet {
  private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
    try {
      int deletedCount = refreshTokenBlackListRepository.deleteExpiredTokens(LocalDateTime.now());
      if (deletedCount == 0) {
        log.info("만료된 토큰이 없습니다.");
      } else {
        log.info("배치 작업 실행: 블랙리스트에서 만료된 토큰 {}개 삭제 완료", deletedCount);
      }
    } catch (Exception e) {
      log.error("만료 토큰 삭제 실패: {}", e.getMessage());
      throw e;
    }
    return RepeatStatus.FINISHED;
  }
}
