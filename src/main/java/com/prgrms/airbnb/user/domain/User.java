package com.prgrms.airbnb.user.domain;

import com.prgrms.airbnb.auth.domain.Group;
import com.prgrms.airbnb.common.jpa.EmailConverter;
import com.prgrms.airbnb.common.jpa.PhoneConverter;
import com.prgrms.airbnb.common.model.BaseEntity;
import com.prgrms.airbnb.common.model.Email;
import com.prgrms.airbnb.common.model.Phone;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @Convert(converter = EmailConverter.class)
  private Email email;

  @Convert(converter = PhoneConverter.class)
  private Phone phone;

  @Enumerated(value = EnumType.STRING)
  private UserType userType;

  @Column(name = "provider")
  private String provider;

  @Column(name = "provider_id")
  private String providerId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id")
  private Group group;

  @Builder
  public User(String name, Email email, Phone phone, UserType userType) {
    setName(name);
    setEmail(email);
    setPhone(phone);
    setUserType(userType);
  }

  public User(String name, Email email, String provider, String providerId, Group group) {
    setName(name);
    setEmail(email);
    setProvider(provider);
    setProviderId(providerId);
    setGroup(group);
  }

  public void addPhone(Phone phone) {
    setPhone(phone);
  }

  private void setName(String name) {
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException();
    }
    this.name = name;
  }

  private void setEmail(Email email) {
    this.email = email;
  }

  private void setPhone(Phone phone) {
    this.phone = phone;
  }

  private void setUserType(UserType userType) {
    this.userType = userType;
  }

  private void setProvider(String provider) {
    this.provider = provider;
  }

  private void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  private void setGroup(Group group) {
    this.group = group;
  }

}
