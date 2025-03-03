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
import com.example.demo.member.entity.repository.RefreshTokenBlackListRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

  @Bean
  public Job blacklistCleanupJob(JobRepository jobRepository, Step tokenCleanupStep) {
    return new JobBuilder("blacklistCleanupJob", jobRepository)
        .start(tokenCleanupStep)
        .preventRestart()
        .build();
  }

  @Bean
  public Step blacklistCleanupStep(
      JobRepository jobRepository,
      Tasklet tokenCleanupTasklet,
      PlatformTransactionManager transactionManager) {
    return new StepBuilder("blacklistCleanupStep", jobRepository)
        .tasklet(tokenCleanupTasklet, transactionManager)
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
