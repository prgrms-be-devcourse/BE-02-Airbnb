package com.prgrms.airbnb.room.domain;

import com.prgrms.airbnb.common.jpa.MoneyConverter;
import com.prgrms.airbnb.common.model.Address;
import com.prgrms.airbnb.common.model.BaseEntity;
import com.prgrms.airbnb.common.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    @OrderColumn(name = "image_idx")
    private List<RoomImage> images = new ArrayList<>();

    private Long userId;

    @Builder
    public Room(Address address, Money charge, String description, Integer maxGuest, List<RoomImage> images, Long userId) {
        this.address = address;
        this.charge = charge;
        this.description = description;
        this.maxGuest = maxGuest;
        this.images.addAll(images);
        this.userId = userId;
    }

    public List<RoomImage> getImages() {
        return Collections.unmodifiableList(images);
    }

    public void changeImages(List<RoomImage> newImages) {
        images.clear();
        images.addAll(newImages);
    }


}
