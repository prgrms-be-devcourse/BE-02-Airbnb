package com.prgrms.airbnb.domain.user.service;

import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.user.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Nested
  @DisplayName("사용자 가입 테스트")
  class JoinTest {
    List<User> userList;

    @BeforeEach
    void setUp() {
      userList = new ArrayList<>();
    }

    @ParameterizedTest
    @CsvSource(value = {"'무송', 'profile_image', 'songe08@gmail.com'", "'송무송', 'profile_image', 'real.purple@gmail.com'"})
    @DisplayName("성공: 각 항목에 알맞은 값이 기입되면 정상적으로 사용자가 가입됩니다.")
    void success(String nickName, String profileImage, String email) {
      // Given, When
      User user = userService.join(
          mockOAuth2User(nickName, getAttributes(nickName, profileImage, email)),
          "kakao"
      );
      // Then
      assertThat(user).isNotNull();
      assertThat(user.getName()).isEqualTo(nickName);
      assertThat(user.getProfileImage()).isEqualTo(profileImage);
      assertThat(user.getEmail()).isEqualTo(new Email(email));
    }

    @ParameterizedTest
    @CsvSource(value = {"'무송', 'profile_image', 'songe08@gmail.com'", "'송무송', 'profile_image', 'real.purple@gmail.com'"})
    @DisplayName("성공: 프로필이 널값이어도 정상적으로 생성됩니다.")
    void successByProfileImage(String nickName, String profileImage, String email) {
      // Given, When
      User user = userService.join(
          mockOAuth2User(nickName, getAttributes(nickName, null, email)),
          "kakao"
      );
      // Then
      assertThat(user).isNotNull();
      assertThat(user.getName()).isEqualTo(nickName);
      assertThat(user.getProfileImage()).isNull();
      assertThat(user.getEmail()).isEqualTo(new Email(email));
    }

    @ParameterizedTest
    @CsvSource(value = {"'무송', 'profile_image'", "'송무송', 'profile_image'"})
    @DisplayName("실패: 이메일이 널값이면 예외를 반환합니다.")
    void failByEmail(String nickName, String profileImage) {
      // Given, When
      Throwable response = catchThrowable(() -> userService.join(
          mockOAuth2User(nickName, getAttributes(nickName, profileImage, null)),
          "kakao"
      ));
      // Then
      assertThat(response).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"'무송', 'profile_image', 'songe08@gmail.com'", "'송무송', 'profile_image', 'real.purple@gmail.com'"})
    @DisplayName("실패: 이름이 널값이면 예외를 반환합니다.")
    void failByName(String nickName, String profileImage, String email) {
      // Given, When
      Throwable response = catchThrowable(() -> userService.join(
          mockOAuth2User(nickName, getAttributes(null, profileImage, email)),
          "kakao"
      ));
      // Then
      assertThat(response).isInstanceOf(IllegalArgumentException.class);
    }
  }

  private Map<String, Object> getAttributes(String nickname, String profileImage, String email) {
    Map<String, String> properties = new HashMap<>();
    properties.put("nickname", nickname);
    properties.put("profile_image", profileImage);

    Map<String, String> kakaoAccount = new HashMap<>();
    kakaoAccount.put("email", email);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("properties", properties);
    attributes.put("kakao_account", kakaoAccount);
    return attributes;
  }

  private OAuth2User mockOAuth2User(String name, Map<String, Object> attributes) {
    OAuth2User oauth2User = mock(OAuth2User.class);
    given(oauth2User.getName()).willReturn(name);
    given(oauth2User.getAttributes()).willReturn(attributes);
    return oauth2User;
  }
}