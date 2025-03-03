package com.example.demo.member.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlackListBatchScheduler {
  private final JobLauncher jobLauncher;
  private final Job blackListCleanupJob;

  @Scheduled(cron = "0 0 0,6,12,18 * * ?")
  public void runBlacklistCleanupJob() {
    try {
      jobLauncher.run(
          blackListCleanupJob,
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters());
      log.info("배치 작업 실행 완료: 블랙리스트 토큰 삭제");
    } catch (JobExecutionException e) {
      log.error("배치 실행 중 오류 발생 : {}", e.getMessage(), e);
    }
  }
}
