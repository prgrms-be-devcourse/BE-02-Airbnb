package com.prgrms.airbnb.room.domain;

import com.prgrms.airbnb.common.jpa.MoneyConverter;
import com.prgrms.airbnb.common.model.BaseEntity;
import com.prgrms.airbnb.common.model.Money;
import com.prgrms.airbnb.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Address;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@Table(name = "rooms")
public class Room extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    @Convert(converter = MoneyConverter.class)
    private Money charge;

    private String description;

    private Integer maxGuest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    public Room(Address address, Money charge, String description, Integer maxGuest, User user) {
        this.address = address;
        this.charge = charge;
        this.description = description;
        this.maxGuest = maxGuest;
        this.user = user;
    }
}
