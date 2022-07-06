package com.prgrms.airbnb.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_permission")
@NoArgsConstructor
public class GroupPermission {

  @Id
  @GeneratedValue
  @Column(name = "id")
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