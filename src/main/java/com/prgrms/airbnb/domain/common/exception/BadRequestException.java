package com.prgrms.airbnb.domain.common.exception;

public class BadRequestException extends RuntimeException {

  public BadRequestException(String msg) {
    super(msg);
  }
}
