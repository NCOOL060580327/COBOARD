package com.example.demo.post;

public enum PostTestConst {
  TEST_ID("1"),
  TEST_TITLE("testPost"),
  TEST_CONTENT("testContent"),
  TEST_COUNT("0");

  private final String value;

  PostTestConst(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
