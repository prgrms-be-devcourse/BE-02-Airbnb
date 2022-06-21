package com.prgrms.airbnb.room.repository;

import com.prgrms.airbnb.room.domain.Room;
import com.prgrms.airbnb.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.room.dto.SearchRoomRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RoomCustomRepository {

  Slice<RoomSummaryResponse> findAll(SearchRoomRequest searchRoomRequest, Pageable pageable);


}
