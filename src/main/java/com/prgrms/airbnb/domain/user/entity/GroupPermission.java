package com.prgrms.airbnb.domain.user.entity;

import javax.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_permission")
@NoArgsConstructor
public class GroupPermission {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id")
  private Group group;

  @ManyToOne(optional = false)
  @JoinColumn(name = "permission_id")
  private Permission permission;

  public GroupPermission(Group group, Permission permission) {
    this.group = group;
    this.permission = permission;
  }

  public Long getId() {
    return id;
  }

  public Group getGroup() {
    return group;
  }

  public Permission getPermission() {
    return permission;
  }

}