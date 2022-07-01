package com.prgrms.airbnb.domain.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler(UnAuthorizedAccessException.class)
  public ResponseEntity<ErrorResponse> unAuthorizedAccessException(UnAuthorizedAccessException e) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorType.UN_AUTHORIZED_ACCESS);
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorType.NOT_FOUND);
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidParamException.class)
  public ResponseEntity<ErrorResponse> invalidParamException(InvalidParamException e) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorType.INVALID_PARAM);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> badRequestException(BadRequestException e) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorType.BAD_REQUEST);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<ErrorResponse> internalServerErrorException(
      InternalServerErrorException e) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorType.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exception(Exception e) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorType.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
