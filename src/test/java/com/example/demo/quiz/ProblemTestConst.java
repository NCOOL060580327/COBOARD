package com.example.demo.quiz;

public enum ProblemTestConst {
  TEST_TITLE("Test Problem"),
  TEST_DESCRIPTION("testDescription"),
  TEST_SOURCE("testSource"),
  TEST_PROBLEM_LINK("testProblemLink"),
  ;

  private final String value;

  ProblemTestConst(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
