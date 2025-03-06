package com.example.demo.board;

public enum BoardTestConst {
  TEST_ID("1"),
  TEST_NAME("testBoard"),
  TEST_THUMBNAIL("testThumbnail"),
  DEFAULT_THUMBNAIL("default-thumbnail.png"),
  ;

  private final String value;

  BoardTestConst(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
