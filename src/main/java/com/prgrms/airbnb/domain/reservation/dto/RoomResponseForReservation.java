package com.prgrms.airbnb.domain.reservation.dto;//package com.prgrms.airbnb.reservation.dto;

import com.prgrms.airbnb.domain.common.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseForReservation {

  private Long id;
  private String name;
  private Address address;
}