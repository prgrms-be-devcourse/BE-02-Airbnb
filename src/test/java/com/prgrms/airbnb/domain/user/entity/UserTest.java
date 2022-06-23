package com.prgrms.airbnb.domain.user.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.airbnb.domain.common.entity.Email;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserTest {

  @Nested
  class 생성 {

    @Test
    @DisplayName("성공: 유저 생성에 성공합니다.")
    public void success() throws Exception {

      Group group = new Group();
      User user = new User("userName",
          "provider",
          "providerId",
          "profileImage",
          group,
          null);

      assertThat(user.getName()).isEqualTo("userName");
      assertThat(user.getProvider()).isEqualTo("provider");
      assertThat(user.getProviderId()).isEqualTo("providerId");
      assertThat(user.getProfileImage()).isEqualTo("profileImage");
      assertThat(user.getGroup()).isEqualTo(group);
      assertThat(user.getEmail()).isNull();

      Email email = new Email("a@naver.com");
      User userGetEmail = new User("userName",
          "provider",
          "providerId",
          "profileImage",
          group,
          email);

      assertThat(userGetEmail.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("실패: 이름이 null 일 경우 -> 예외를 반환")
    public void failName1() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              null,
              "provider",
              "providerId",
              "profileImage",
              group,
              null));
    }

    @Test
    @DisplayName("실패: 이름이 empty 일 경우 -> 예외를 반환")
    public void failName2() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              "",
              "provider",
              "providerId",
              "profileImage",
              group,
              null));
    }

    @Test
    @DisplayName("실패: 이름이 blank 일 경우 -> 예외를 반환")
    public void failName3() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              " ",
              "provider",
              "providerId",
              "profileImage",
              group,
              null));
    }

    @Test
    @DisplayName("실패: provider 이 null 일 경우 -> 예외를 반환")
    public void failProvider1() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              "name",
              null,
              "providerId",
              "profileImage",
              group,
              null));
    }

    @Test
    @DisplayName("실패: provider 이 empty 일 경우 -> 예외를 반환")
    public void failProvider2() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              "name",
              "",
              "providerId",
              "profileImage",
              group,
              null));
    }

    @Test
    @DisplayName("실패: provider 이 Blank 일 경우 -> 예외를 반환")
    public void failProvider3() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              "name",
              " ",
              "providerId",
              "profileImage",
              group,
              null));
    }

    @Test
    @DisplayName("실패: providerId 이 null 일 경우 -> 예외를 반환")
    public void failProviderId1() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              "name",
              "provider",
              null,
              "profileImage",
              group,
              null));
    }

    @Test
    @DisplayName("실패: providerId 이 empty 일 경우 -> 예외를 반환")
    public void failProviderId2() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              "name",
              "provider",
              "",
              "profileImage",
              group,
              null));
    }

    @Test
    @DisplayName("실패: providerId 이 blank 일 경우 -> 예외를 반환")
    public void failProviderId3() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              "name",
              "provider",
              " ",
              "profileImage",
              group,
              null));
    }

    @Test
    @DisplayName("실패: group 이 null 일 경우 -> 예외를 반환")
    public void failGroup() throws Exception {

      //given
      Group group = new Group();

      assertThrows(RuntimeException.class,
          () -> new User(
              "name",
              "provider",
              "providerId",
              "profileImage",
              null,
              null));
    }



  }







}