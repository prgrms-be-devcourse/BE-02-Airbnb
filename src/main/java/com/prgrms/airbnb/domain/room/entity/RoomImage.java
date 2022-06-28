package com.prgrms.airbnb.domain.room.entity;

import com.prgrms.airbnb.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "room_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomImage extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "path")
  private String path;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
  private Room room;

  public RoomImage(String path) {
    this.path = path;
  }

  public void setRoom(Room room) {
    if (this.room != null) {
      this.room.getRoomImages().remove(this);
    }
    this.room = room;
    room.getRoomImages().add(this);
  }

  public void deleteRoom() {
    this.room.getRoomImages().remove(this);
    this.room = null;
  }
}
