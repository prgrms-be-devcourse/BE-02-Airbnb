package com.prgrms.airbnb.domain.common.exception;

public class NotFoundException extends RuntimeException {


  public NotFoundException(String msg) {
    super(msg);
  }
}
