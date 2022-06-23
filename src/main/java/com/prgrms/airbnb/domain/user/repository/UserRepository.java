package com.prgrms.airbnb.domain.user.repository;

import com.prgrms.airbnb.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.group g left join fetch g.permissions gp join fetch gp.permission where" +
        " u.name = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("select u from User u join fetch u.group g left join fetch g.permissions gp join fetch gp.permission where" +
        " u.provider = :provider and u.providerId = :providerId")
    Optional<User> findByProviderAndProviderId(@Param("provider") String provider, @Param("providerId") String providerId);

}