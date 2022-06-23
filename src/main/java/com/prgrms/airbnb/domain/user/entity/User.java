package com.prgrms.airbnb.domain.user.entity;

import com.prgrms.airbnb.domain.common.entity.BaseEntity;
import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.common.entity.Phone;
import com.prgrms.airbnb.domain.common.converter.EmailConverter;
import com.prgrms.airbnb.domain.common.converter.PhoneConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "profile_image")
  private String profileImage;

  @Convert(converter = EmailConverter.class)
  @Column(name = "email")
  private Email email;

  @Convert(converter = PhoneConverter.class)
  @Column(name = "phone")
  private Phone phone;

  @Column(name = "provider")
  private String provider;

  @Column(name = "provider_id")
  private String providerId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id")
  private Group group;

  public User(String name, String provider, String providerId, String profileImage, Group group,
      Email email) {
    setName(name);
    setEmail(email);
    setProvider(provider);
    setProviderId(providerId);
    setProfileImage(profileImage);
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

  private void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  private void setEmail(Email email) {
    this.email = email;
  }

  private void setPhone(Phone phone) {
    this.phone = phone;
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