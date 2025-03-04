package com.example.demo.member.batch;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import com.example.demo.member.entity.RefreshTokenBlackList;
import com.example.demo.member.entity.repository.RefreshTokenBlackListRepository;

@Component
public class RefreshTokenItemReader implements ItemReader<RefreshTokenBlackList> {

  private final RefreshTokenBlackListRepository repository;
  private Iterator<RefreshTokenBlackList> iterator;

  public RefreshTokenItemReader(RefreshTokenBlackListRepository repository) {
    this.repository = repository;
  }

  @Override
  public RefreshTokenBlackList read() {
    if (iterator == null || !iterator.hasNext()) {
      List<RefreshTokenBlackList> expiredTokens =
          repository.findAllByExpiredAtBefore(LocalDateTime.now());
      iterator = expiredTokens.iterator();
    }
    return iterator.hasNext() ? iterator.next() : null;
  }
}
