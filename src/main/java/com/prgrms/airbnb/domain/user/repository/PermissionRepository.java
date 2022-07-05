package com.prgrms.airbnb.domain.user.repository;

import com.prgrms.airbnb.domain.user.entity.Permission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
  Optional<Permission> findByName(String name);
}
