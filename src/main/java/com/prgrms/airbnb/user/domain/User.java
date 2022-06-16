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
        this.email = email;
        this.phone = phone;
    }

    private void setName(String name) {
        this.name = name;
    }
}
