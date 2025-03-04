package com.example.demo.global.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.member.batch.BlackListCleanUpTasklet;
import com.example.demo.member.batch.RefreshTokenItemReader;
import com.example.demo.member.batch.RefreshTokenItemWriter;
import com.example.demo.member.entity.RefreshTokenBlackList;
import com.example.demo.member.entity.repository.RefreshTokenBlackListRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

  @Bean
  public Job blacklistCleanupTaskletJob(JobRepository jobRepository, Step blacklistCleanupStep) {
    return new JobBuilder("blacklistCleanuptaskletJob", jobRepository)
        .start(blacklistCleanupStep)
        .preventRestart()
        .build();
  }

  @Bean
  public Job blacklistCleanupChunkJob(JobRepository jobRepository, Step blacklistCleanupChunkStep) {
    return new JobBuilder("blacklistCleanupChunkJob", jobRepository)
        .start(blacklistCleanupChunkStep)
        .preventRestart()
        .build();
  }

  @Bean
  public Step blacklistCleanupStep(
      JobRepository jobRepository,
      Tasklet blacklistCleanupTasklet,
      PlatformTransactionManager transactionManager) {
    return new StepBuilder("blacklistCleanupStep", jobRepository)
        .tasklet(blacklistCleanupTasklet, transactionManager)
        .build();
  }

  @Bean
  public Step blacklistCleanupChunkStep(
      JobRepository jobRepository,
      RefreshTokenItemReader itemReader,
      RefreshTokenItemWriter itemWriter,
      PlatformTransactionManager transactionManager) {
    return new StepBuilder("blacklistCleanupChunkStep", jobRepository)
        .<RefreshTokenBlackList, RefreshTokenBlackList>chunk(1000, transactionManager)
        .reader(itemReader)
        .writer(itemWriter)
        .build();
  }

  @Bean
  public Tasklet blacklistCleanupTasklet(
      RefreshTokenBlackListRepository refreshTokenBlackListRepository) {
    return new BlackListCleanUpTasklet(refreshTokenBlackListRepository);
  }

  @Bean
  public JobRepository jobRepository(
      DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
    JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setTransactionManager(transactionManager);
    factoryBean.afterPropertiesSet();
    return factoryBean.getObject();
  }
}
