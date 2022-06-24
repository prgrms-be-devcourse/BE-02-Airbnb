package com.prgrms.airbnb.domain.room.repository;

import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.SearchRoomRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RoomSearchRepository {

  Slice<RoomSummaryResponse> searchRoom(SearchRoomRequest searchRoomRequest, Pageable pageable);
}
