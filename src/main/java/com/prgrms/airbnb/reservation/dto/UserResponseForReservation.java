package com.prgrms.airbnb.reservation.dto;

import com.prgrms.airbnb.common.model.Email;
import com.prgrms.airbnb.common.model.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseForReservation {
    private Long id;
    private String name;
    private Email email;
    private Phone phoneNumber;
}