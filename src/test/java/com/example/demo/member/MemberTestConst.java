package com.example.demo.member;

public enum MemberTestConst {
  TEST_ID("1"),
  TEST_EMAIL("test@example.com"),
  TEST_PASSWORD("Test1234!@#$"),
  TEST_NICKNAME("testUser"),
  TEST_PROFILE_IMAGE("profile.jpg"),
  TEST_ACCESS_TOKEN("testAccessToken"),
  TEST_REFRESH_TOKEN("testRefreshToken"),
  TEST_IDENTIFIED_CODE("testIdentifiedCode"),
  ;

  private final String value;

  MemberTestConst(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
