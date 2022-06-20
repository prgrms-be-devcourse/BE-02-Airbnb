package com.prgrms.airbnb.auth.repository;

import com.prgrms.airbnb.auth.domain.Group;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

  Optional<Group> findByName(String name);

}
