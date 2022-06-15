package com.prgrms.airbnb.room.domain;

import com.prgrms.airbnb.common.model.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Getter
public class RoomImage extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String path;

    protected RoomImage() {
    }

    public RoomImage(String path) {
        this.path = path;
    }
}
