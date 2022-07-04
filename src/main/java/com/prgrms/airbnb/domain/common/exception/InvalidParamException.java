package com.prgrms.airbnb.domain.common.exception;

public class InvalidParamException extends RuntimeException {

  public InvalidParamException(String msg) {
    super(msg);
  }
}
