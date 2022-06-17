package com.prgrms.airbnb.reservation.dto;

import com.prgrms.airbnb.common.model.Address;
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