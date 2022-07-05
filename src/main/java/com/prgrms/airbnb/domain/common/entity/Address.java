package com.prgrms.airbnb.domain.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(value = AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Address {

  @ApiModelProperty(example = "시, 도, 군")
  private String address1;

  @ApiModelProperty(example = "상세 주소")
  private String address2;

  public Address(String address1, String address2) {
    this.address1 = address1;
    this.address2 = address2;
  }

  public String getAddress1() {
    return address1;
  }

  public String getAddress2() {
    return address2;
  }
}
