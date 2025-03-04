package com.example.demo.member.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BlackListBatchScheduler {
  private final JobLauncher jobLauncher;

  private final Job blackListCleanupTaskletJob;

  private final Job blacklistCleanupChunkJob;

  public BlackListBatchScheduler(
      JobLauncher jobLauncher,
      @Qualifier("blacklistCleanupTaskletJob") Job blackListCleanupTaskletJob,
      @Qualifier("blacklistCleanupChunkJob") Job blacklistCleanupChunkJob) {
    this.jobLauncher = jobLauncher;
    this.blackListCleanupTaskletJob = blackListCleanupTaskletJob;
    this.blacklistCleanupChunkJob = blacklistCleanupChunkJob;
  }

  @Scheduled(cron = "0 0 0,6,12,18 * * ?")
  public void runBlacklistCleanupTaskletJob() throws JobExecutionException {
    runJob(blackListCleanupTaskletJob, "Tasklet");
  }

  @Scheduled(cron = "0 30 0,6,12,18 * * ?")
  public void runBlacklistCleanupChunkJob() throws JobExecutionException {
    runJob(blacklistCleanupChunkJob, "Chunk");
  }

  private void runJob(Job job, String type) throws JobExecutionException {
    JobExecution jobExecution =
        jobLauncher.run(
            job,
            new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters());

    if (!jobExecution.getStatus().isUnsuccessful()) {
      log.info("배치 작업 실행 완료: {} 기반 블랙리스트 토큰 삭제", type);
    } else {
      log.error(
          "배치 실행 중 오류 발생 : Job status = {}, Failures = {}",
          jobExecution.getStatus(),
          jobExecution.getAllFailureExceptions());
      throw new JobExecutionException("Job failed with status: " + jobExecution.getStatus());
    }
  }
}
