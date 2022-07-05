package com.prgrms.airbnb.domain.user.service;

import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.common.exception.UnAuthorizedAccessException;
import com.prgrms.airbnb.domain.common.service.UploadService;
import com.prgrms.airbnb.domain.user.dto.UserDetailResponse;
import com.prgrms.airbnb.domain.user.dto.UserUpdateRequest;
import com.prgrms.airbnb.domain.user.entity.Group;
import com.prgrms.airbnb.domain.user.entity.GroupPermission;
import com.prgrms.airbnb.domain.user.entity.Permission;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.GroupPermissionRepository;
import com.prgrms.airbnb.domain.user.repository.GroupRepository;
import com.prgrms.airbnb.domain.user.repository.PermissionRepository;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import com.prgrms.airbnb.domain.user.util.UserConverter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {

  @Value("${cloud.aws.s3.bucket.url}")
  private String s3Cdn;

  private final UserRepository userRepository;
  private final GroupRepository groupRepository;
  private final PermissionRepository permissionRepository;
  private final GroupPermissionRepository groupPermissionRepository;
  private final UploadService uploadService;

  public UserService(UserRepository userRepository, GroupRepository groupRepository,
      PermissionRepository permissionRepository,
      GroupPermissionRepository groupPermissionRepository, UploadService uploadService) {
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
    this.permissionRepository = permissionRepository;
    this.groupPermissionRepository = groupPermissionRepository;
    this.uploadService = uploadService;
  }

  @Transactional
  public User join(OAuth2User oauth2User, String authorizedClientRegistrationId) {
    String providerId = oauth2User.getName();
    return findByProviderAndProviderId(authorizedClientRegistrationId, providerId)
        .orElseGet(() -> {
          Map<String, Object> attributes = oauth2User.getAttributes();
          @SuppressWarnings("unchecked")
          Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
          Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

          String nickname = (String) properties.get("nickname");
          String profileImage = (String) properties.get("profile_image");
          String email = (String) kakaoAccount.get("email");
          Email newEmail = Objects.isNull(email) ? null : new Email(email);
          Group group = groupRepository.findByName("USER_GROUP").orElse(initUserGroupPermission());
          return userRepository.save(
              new User(nickname, authorizedClientRegistrationId, providerId, profileImage, group,
                  newEmail));
        });
  }

  public Optional<UserDetailResponse> findById(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> {
      throw new UnAuthorizedAccessException(this.getClass().getName());
    });
    return Optional.of(user).map(UserConverter::from);
  }

  public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
    return userRepository.findByProviderAndProviderId(provider, providerId);
  }

  @Transactional
  public UserDetailResponse modify(Long userId, UserUpdateRequest request,
      MultipartFile multipartFile) {
    User user = userRepository.findById(userId).orElseThrow(() -> {
      throw new UnAuthorizedAccessException(this.getClass().getName());
    });

    if (Objects.nonNull(multipartFile)) {
      if (Objects.nonNull(user.getProfileImage()) && user.getProfileImage().startsWith(s3Cdn)) {
        uploadService.delete(user.getProfileImage());
      }
      String url = uploadService.uploadImg(multipartFile);
      user.changeProfileImage(url);
    }
    user.changeName(request.getName());
    user.changeEmail(request.getEmail());
    user.changePhone(request.getPhone());
    userRepository.save(user);
    return UserConverter.from(user);
  }

  @Transactional
  public UserDetailResponse changeUserToHost(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> {
      throw new UnAuthorizedAccessException(this.getClass().getName());
    });
    Group group = groupRepository.findByName("HOST_GROUP").orElse(initHostGroupPermission());
    user.changeGroup(group);
    return UserConverter.from(user);
  }

  private Group initUserGroupPermission() {
    Permission permission = permissionRepository.save(new Permission("ROLE_USER"));
    Group group = groupRepository.save(new Group("USER_GROUP"));
    groupPermissionRepository.save(new GroupPermission(group, permission));
    return group;
  }

  private Group initHostGroupPermission() {
    Permission permission = permissionRepository.save(new Permission("ROLE_HOST"));
    Group group = groupRepository.save(new Group("HOST_GROUP"));
    groupPermissionRepository.save(new GroupPermission(group, permission));
    return group;
  }

  private Group initAdminGroupPermission() {
    Permission permission = permissionRepository.save(new Permission("ROLE_ADMIN"));
    Group group = groupRepository.save(new Group("ADMIN_GROUP"));
    groupPermissionRepository.save(new GroupPermission(group, permission));
    return group;
  }
}