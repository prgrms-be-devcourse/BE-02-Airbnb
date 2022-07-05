package com.prgrms.airbnb.domain.user.entity;

import com.prgrms.airbnb.domain.common.converter.EmailConverter;
import com.prgrms.airbnb.domain.common.converter.PhoneConverter;
import com.prgrms.airbnb.domain.common.entity.BaseEntity;
import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.common.entity.Phone;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

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

  private void setName(String name) {
    checkBlank(name);
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
    checkBlank(provider);
    this.provider = provider;
  }

  private void setProviderId(String providerId) {
    checkBlank(providerId);
    this.providerId = providerId;
  }

  private void setGroup(Group group) {
    if (ObjectUtils.isEmpty(group)) {
      throw new IllegalArgumentException();
    }
    this.group = group;
  }

  private void checkBlank(String target) {
    if (StringUtils.isBlank(target)) {
      throw new IllegalArgumentException();
    }
  }

  public void changeName(String name) {
    setName(name);
  }

  public void changeEmail(String email) {
    setEmail(new Email(email));
  }

  public void changeProfileImage(String imageUrl) {
    setProfileImage(imageUrl);
  }

  public void changePhone(String phone) {
    if (Objects.isNull(phone)) {
      setPhone(null);
    } else {
      setPhone(new Phone(phone));
    }
  }

  public void changeGroup(Group group) {
    setGroup(group);
  }
}