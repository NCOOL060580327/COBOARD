package com.example.demo.member.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.demo.member.entity.RefreshTokenBlackList;
import com.example.demo.member.entity.repository.RefreshTokenBlackListRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenItemWriter implements ItemWriter<RefreshTokenBlackList> {

  private final RefreshTokenBlackListRepository repository;

  @Override
  public void write(Chunk<? extends RefreshTokenBlackList> items) {
    try {
      List<RefreshTokenBlackList> refreshTokenBlackLists = new ArrayList<>(items.getItems());
      repository.deleteAllInBatch(refreshTokenBlackLists);
      log.info("Chunk 기반으로 {}개 토큰 삭제 완료", refreshTokenBlackLists.size());
    } catch (Exception e) {
      log.error("Chunk 기반 삭제 실패: {}", e.getMessage());
      throw e;
    }
  }
}
