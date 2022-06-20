package com.prgrms.airbnb.auth.service;

import com.prgrms.airbnb.auth.domain.Group;
import com.prgrms.airbnb.auth.repository.GroupRepository;
import com.prgrms.airbnb.auth.repository.OAuthRepository;
import com.prgrms.airbnb.common.model.Email;
import com.prgrms.airbnb.user.domain.User;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
public class OAuthService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final OAuthRepository oAuthRepository;

  private final GroupRepository groupRepository;

  public OAuthService(OAuthRepository oAuthRepository,
      GroupRepository groupRepository) {
    this.oAuthRepository = oAuthRepository;
    this.groupRepository = groupRepository;
  }

  @Transactional(readOnly = true)
  public Optional<User> findByUsername(String username) {
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException();
    }
    return oAuthRepository.findByName(username);
  }

  @Transactional(readOnly = true)
  public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
    if (StringUtils.isBlank(provider)) {
      throw new IllegalArgumentException();
    }
    if (StringUtils.isBlank(providerId)) {
      throw new IllegalArgumentException();
    }

    return oAuthRepository.findByProviderAndProviderId(provider, providerId);
  }


  @Transactional
  public User join(OAuth2User oauth2User, String authorizedClientRegistrationId) {
    if (ObjectUtils.isEmpty(oauth2User)) {
      throw new IllegalArgumentException();
    }
    if (StringUtils.isBlank(authorizedClientRegistrationId)) {
      throw new IllegalArgumentException();
    }

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

          if (ObjectUtils.isEmpty(properties)) {
            throw new IllegalArgumentException();
          }
          String nickname = (String) properties.get("nickname");
          String email = (String)kakaoAccount.get("email");
          //TODO: email이 비어있을 가능성이 있다
          Group group = groupRepository.findByName("USER_GROUP")
              .orElseThrow(() -> new IllegalStateException("Could not found group for USER_GROUP"));
          return oAuthRepository.save(
              new User(nickname,new Email(email),authorizedClientRegistrationId, providerId, group)
          );
        });
  }

}