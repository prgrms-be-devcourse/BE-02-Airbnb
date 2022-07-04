package com.prgrms.airbnb.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LogConfigure {

  @Around("within(com.prgrms.airbnb.domain.common.exception..*)")
  public Object logging(ProceedingJoinPoint pjp) throws Throwable {
    long now = System.currentTimeMillis();
    if (pjp.getArgs()[0] instanceof RuntimeException) {
      RuntimeException e = (RuntimeException) pjp.getArgs()[0];
      log.warn("Error : {}({}) = {}", e.getClass(), e.getMessage(), now);
    }
    Object result = pjp.proceed();
    return result;
  }
}
