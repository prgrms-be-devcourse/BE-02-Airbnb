package com.prgrms.airbnb.domain.common.exception;

public enum ErrorType {
  UN_AUTHORIZED_ACCESS("접근 권한이 없습니다."), NOT_FOUND("존재하지 않는 데이터입니다."), INVALID_PARAM(
      "잘못된 입력입니다."), BAD_REQUEST("잘못된 요청입니다."), INTERNAL_SERVER_ERROR("알 수 없는 에러입니다.");

  private final String msg;

  ErrorType(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}
