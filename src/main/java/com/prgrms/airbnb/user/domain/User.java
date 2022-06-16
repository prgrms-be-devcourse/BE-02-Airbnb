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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Convert(converter = EmailConverter.class)
    private Email email;

    @Convert(converter = PhoneConverter.class)
    private Phone phone;

    @Builder
    public User(String name, Email email, Phone phone) {
        setName(name);
        setEmail(email);
        setPhone(phone);
    }

    private void setName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
