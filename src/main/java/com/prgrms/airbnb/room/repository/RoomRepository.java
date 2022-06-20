package com.prgrms.airbnb.room.repository;

import com.prgrms.airbnb.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomCustomRepository {

}
