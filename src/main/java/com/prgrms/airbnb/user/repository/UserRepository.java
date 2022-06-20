package com.prgrms.airbnb.user.repository;

import com.prgrms.airbnb.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {


}