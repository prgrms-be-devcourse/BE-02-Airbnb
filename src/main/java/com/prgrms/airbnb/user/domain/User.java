package com.prgrms.airbnb.user.domain;

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

    @Builder
    public User(String name, Email email, Phone phone, UserType userType) {
        setName(name);
        setEmail(email);
        setPhone(phone);
        setUserType(userType);
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
}
