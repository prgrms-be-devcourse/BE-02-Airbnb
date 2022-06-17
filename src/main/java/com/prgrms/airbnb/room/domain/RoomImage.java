package com.prgrms.airbnb.room.domain;

import com.prgrms.airbnb.common.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomImage extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String path;

    public RoomImage(String path) {
        this.path = path;
    }


}
