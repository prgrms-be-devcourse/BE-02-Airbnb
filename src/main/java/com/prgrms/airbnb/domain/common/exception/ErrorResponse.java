package com.prgrms.airbnb.domain.common.exception;

public class ErrorResponse {

  private String error;
  private String msg;

  public ErrorResponse(ErrorType errorType) {
    this.error = errorType.name();
    this.msg = errorType.getMsg();
  }
}
