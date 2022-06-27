package com.prgrms.airbnb.domain.user.service;

import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.user.dto.UserDetailResponse;
import com.prgrms.airbnb.domain.user.dto.UserUpdateRequest;
import com.prgrms.airbnb.domain.user.entity.Group;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.GroupRepository;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import com.prgrms.airbnb.domain.user.util.UserConverter;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final UserRepository userRepository;
  private final GroupRepository groupRepository;

  public UserService(UserRepository userRepository, GroupRepository groupRepository) {
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
  }

  @Transactional
  public User join(OAuth2User oauth2User, String authorizedClientRegistrationId) {
    String providerId = oauth2User.getName();
    return findByProviderAndProviderId(authorizedClientRegistrationId, providerId)
        .map(user -> {
          log.warn("Already exists: {} for (provider: {}, providerId: {})", user,
              authorizedClientRegistrationId, providerId);
          return user;
        })
        .orElseGet(() -> {
          Map<String, Object> attributes = oauth2User.getAttributes();
          @SuppressWarnings("unchecked")
          Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
          Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

          String nickname = (String) properties.get("nickname");
          String profileImage = (String) properties.get("profile_image");
          String email = (String) kakaoAccount.get("email");
          Group group = groupRepository.findByName("USER_GROUP")
              .orElseThrow(() -> new IllegalStateException("Could not found group for USER_GROUP"));
          return userRepository.save(
              new User(nickname, authorizedClientRegistrationId, providerId, profileImage, group,
                  new Email(email))
          );
        });
  }

  public Optional<UserDetailResponse> findById(Long userId) {
    return userRepository.findById(userId).map(UserConverter::from);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
    return userRepository.findByProviderAndProviderId(provider, providerId);
  }

  @Transactional
  public UserDetailResponse modify(Long userId, UserUpdateRequest request) {
    User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
    user.changeName(request.getName());
    user.changeEmail(request.getEmail());
    userRepository.save(user);
    return UserConverter.from(user);
  }
}