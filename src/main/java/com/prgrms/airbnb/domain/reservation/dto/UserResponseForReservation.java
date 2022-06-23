package com.prgrms.airbnb.domain.reservation.dto;

import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.common.entity.Phone;
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