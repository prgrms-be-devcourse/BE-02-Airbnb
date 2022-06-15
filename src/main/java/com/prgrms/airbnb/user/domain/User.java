package com.prgrms.airbnb.user.domain;

import com.prgrms.airbnb.common.Email;
import com.prgrms.airbnb.common.Phone;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Phone phoneNumber;
}
