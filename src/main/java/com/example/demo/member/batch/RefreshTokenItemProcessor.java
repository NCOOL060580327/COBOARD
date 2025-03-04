package com.example.demo.member.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.member.entity.RefreshTokenBlackList;

@Component
public class RefreshTokenItemProcessor
    implements ItemProcessor<RefreshTokenBlackList, RefreshTokenBlackList> {
  @Override
  public RefreshTokenBlackList process(RefreshTokenBlackList item) {
    return item;
  }
}
