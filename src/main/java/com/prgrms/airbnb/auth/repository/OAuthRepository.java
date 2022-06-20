package com.prgrms.airbnb.auth.repository;

import com.prgrms.airbnb.user.domain.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OAuthRepository extends JpaRepository<User, Long> {
  @Query("select u from User u join fetch u.group g left join fetch g.permissions gp join fetch gp.permission where u.name = :name")
  Optional<User> findByName(String name);

  @Query("select u from User u join fetch u.group g left join fetch g.permissions gp join fetch gp.permission where u.provider = :provider and u.providerId = :providerId")
  Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
