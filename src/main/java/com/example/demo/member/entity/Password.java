package com.example.demo.member.entity;

import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.AuthException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

  private static final String PASSWORD_REGEX =
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+-=\\[\\]{}|;':\",./<>?~`\\\\])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?~`\\\\]{9,16}";

  @Column(name = "password", nullable = false, columnDefinition = "TEXT")
  private String password;

  public static Password encryptPassword(String plainPassword, BCryptPasswordEncoder encoder) {
    if (!isPasswordValid(plainPassword)) {
      throw new AuthException(GlobalErrorCode.NOT_VALID_PASSWORD);
    }

    return new Password(encoder.encode(plainPassword));
  }

  public static Boolean isPasswordValid(String plainPassword) {
    return Pattern.matches((PASSWORD_REGEX), plainPassword);
  }

  public Boolean isSamePassword(String plainPassword, BCryptPasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(plainPassword, password);
  }
}
