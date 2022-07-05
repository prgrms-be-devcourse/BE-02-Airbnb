package com.prgrms.airbnb.domain.user.repository;

import com.prgrms.airbnb.domain.user.entity.GroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupPermissionRepository extends JpaRepository<GroupPermission, Long> {

}
