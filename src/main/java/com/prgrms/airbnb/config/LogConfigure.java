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
    log.warn("Error : {}({}) = {}", pjp.getSignature().getDeclaringTypeName(),
        pjp.getSignature().getName(), now);
    Object result = pjp.proceed();
    return result;
  }
}
