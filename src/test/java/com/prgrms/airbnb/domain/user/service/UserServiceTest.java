package com.prgrms.airbnb.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.common.exception.InvalidParamException;
import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.common.exception.UnAuthorizedAccessException;
import com.prgrms.airbnb.domain.common.service.UploadService;
import com.prgrms.airbnb.domain.user.dto.UserDetailResponse;
import com.prgrms.airbnb.domain.user.dto.UserUpdateRequest;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.util.UserConverter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @MockBean
  private UploadService uploadService;

  @Autowired
  private UserService userService;

  @Nested
  @DisplayName("사용자 가입 테스트")
  class JoinTest {

    @ParameterizedTest
    @CsvSource(value = {"'무송', 'profile_image', 'songe08@gmail.com'",
        "'송무송', 'profile_image', 'real.purple@gmail.com'"})
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
    @CsvSource(value = {"'무송', 'songe08@gmail.com'", "'송무송', 'real.purple@gmail.com'"})
    @DisplayName("성공: 프로필이 널값이어도 정상적으로 생성됩니다.")
    void successByProfileImage(String nickName, String email) {
      // Given, When
      User user = userService.join(
          mockOAuth2User(nickName, getAttributes(nickName, null, email)),
          "kakao"
      );
      // Then
      assertThat(user.getName()).isEqualTo(nickName);
      assertThat(user.getProfileImage()).isNull();
      assertThat(user.getEmail()).isEqualTo(new Email(email));
    }

    @ParameterizedTest
    @CsvSource(value = {"'무송', 'profile_image'", "'송무송', 'profile_image'"})
    @DisplayName("성공: 이메일이 널값이어도 정상적으로 생성됩니다.")
    void failByEmail(String nickName, String profileImage) {
      // Given, When
      User user = userService.join(
          mockOAuth2User(nickName, getAttributes(nickName, profileImage, null)),
          "kakao");
      // Then
      assertThat(user.getName()).isEqualTo(nickName);
      assertThat(user.getProfileImage()).isEqualTo(profileImage);
      assertThat(user.getEmail()).isNull();
    }

    @ParameterizedTest
    @CsvSource(value = {"'무송', 'profile_image', 'songe08@gmail.com'",
        "'송무송', 'profile_image', 'real.purple@gmail.com'"})
    @DisplayName("실패: 이름이 널값이면 예외를 반환합니다.")
    void failByName(String nickName, String profileImage, String email) {
      // Given, When
      Throwable response = catchThrowable(() -> userService.join(
          mockOAuth2User(nickName, getAttributes(null, profileImage, email)),
          "kakao"
      ));
      // Then
      assertThat(response).isInstanceOf(InvalidParamException.class);
    }
  }

  @Nested
  @DisplayName("유저 정보 ID로 조회 테스트")
  class FindByIdTest {

    List<User> userList;

    @BeforeEach
    void setUp() {
      userList = new ArrayList<>();
      User user = userService.join(
          mockOAuth2User("moosong", getAttributes("moosong", "profileImage", "songe08@gmail.com")),
          "kakao"
      );
      userList.add(user);
      User user2 = userService.join(
          mockOAuth2User("MS", getAttributes("MS", "profileImage", "real@gmail.com")),
          "kakao"
      );
      userList.add(user2);
    }

    @Test
    @DisplayName("성공 : 존재하는 ID를 조회하는 경우")
    void success() {
      // Given, When, Then
      userList.forEach(user -> {
        UserDetailResponse actual = userService.findById(user.getId()).orElseThrow();
        assertThat(actual).isEqualTo(UserConverter.from(user));
      });
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 ID를 조회하는 경우")
    void fail() {
      // Given, When
      Throwable response = catchThrowable(() -> userService.findById(0L).orElseThrow());
      // Then
      assertThat(response).isInstanceOf(NotFoundException.class);
    }
  }

  @Nested
  @DisplayName("유저 정보 수정 테스트")
  class ModifyTest {

    List<User> userList;
    MockMultipartFile mockMultipartFile;

    @BeforeEach
    void setUp() throws IOException {
      userList = new ArrayList<>();
      User user = userService.join(
          mockOAuth2User("moosong", getAttributes("moosong",
              "http://k.kakaocdn.net/dn/b42VJI/btrC2YcVANJ/0FMEJe7roD0N1IWkFGG8M1/img_640x640.jpg",
              "songe08@gmail.com")),
          "kakao"
      );
      userList.add(user);
      User user2 = userService.join(
          mockOAuth2User("MS", getAttributes("MS",
              "http://k.kakaocdn.net/dn/b42VJI/btrC2YcVANJ/0FMEJe7roD0N1IWkFGG8M1/img_640x640.jpg",
              "real@gmail.com")),
          "kakao"
      );
      userList.add(user2);
      mockMultipartFile = getMockMultipartFile("testCustomerUpload1", "png",
          "src/test/resources/uploadFile/testCustomerUpload1.png");
    }

    @AfterEach
    void tearDown() {
      userList.forEach(user -> uploadService.delete(user.getProfileImage()));
    }

    @Test
    @DisplayName("성공 : 존재하는 ID를 수정하는 경우")
    void success() {
      // Given
      UserUpdateRequest request = UserUpdateRequest.builder()
          .email("tiger@naver.com")
          .name("tiger")
          .phone("010-1234-5678")
          .build();

      when(uploadService.uploadImg(any(MultipartFile.class))).thenReturn("image_url");
      doNothing().when(uploadService).delete(any(String.class));

      // When, Then
      userList.forEach(user -> {
        UserDetailResponse actual = userService.modify(user.getId(), request, mockMultipartFile);
        assertThat(actual).isEqualTo(UserConverter.from(user));
      });
    }

    @Test
    @DisplayName("성공 : 비어있는 이미지로 프로필을 수정하는 경우")
    void successByNullImage() {
      // Given
      UserUpdateRequest request = UserUpdateRequest.builder()
          .email("tiger@naver.com")
          .name("tiger")
          .phone("010-1234-5678")
          .build();
      when(uploadService.uploadImg(any(MultipartFile.class))).thenReturn("image_url");
      doNothing().when(uploadService).delete(any(String.class));
      // When, Then
      userList.forEach(user -> {
        UserDetailResponse actual = userService.modify(user.getId(), request, null);
        assertThat(actual).isEqualTo(UserConverter.from(user));
      });
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 ID를 수정하는 경우 예외가 발생합니다.")
    void fail() {
      // Given
      UserUpdateRequest request = UserUpdateRequest.builder().build();
      when(uploadService.uploadImg(any(MultipartFile.class))).thenReturn("image_url");
      doNothing().when(uploadService).delete(any(String.class));
      // When
      Throwable response = catchThrowable(
          () -> userService.modify(0L, request, mockMultipartFile));
      // Then
      assertThat(response).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("실패 : 변경하고자 하는 이름이 비어있는 경우 예외가 발생합니다.")
    void failByNullName() {
      // Given
      UserUpdateRequest request = UserUpdateRequest.builder()
          .email("tiger@naver.com")
          .build();
      when(uploadService.uploadImg(any(MultipartFile.class))).thenReturn("image_url");
      doNothing().when(uploadService).delete(any(String.class));
      // When, Then
      userList.forEach(user -> {
        Throwable response = catchThrowable(
            () -> userService.modify(user.getId(), request, mockMultipartFile));
        assertThat(response).isInstanceOf(InvalidParamException.class);
      });
    }

    @Test
    @DisplayName("실패 : 변경하고자 하는 이메일이 비어있는 경우 예외가 발생합니다.")
    void failByNullEmail() {
      // Given
      UserUpdateRequest request = UserUpdateRequest.builder()
          .name("tiger")
          .build();
      when(uploadService.uploadImg(any(MultipartFile.class))).thenReturn("image_url");
      doNothing().when(uploadService).delete(any(String.class));
      // When, Then
      userList.forEach(user -> {
        Throwable response = catchThrowable(
            () -> userService.modify(user.getId(), request, mockMultipartFile));
        assertThat(response).isInstanceOf(InvalidParamException.class);
      });
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

  private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path)
      throws IOException {
    FileInputStream fileInputStream = new FileInputStream(path);
    return new MockMultipartFile(
        fileName, fileName + "." + contentType, contentType, fileInputStream);
  }
}