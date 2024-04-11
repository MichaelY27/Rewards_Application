package com.example.reward.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    // Log before any controller method is called
    @Before("com.example.reward.aop.PointCuts.inControllerLayer()")
    public void logBeforeRequest(JoinPoint joinPoint) {
        log.info("New request: Invoking method: {}()", joinPoint.getSignature().getName());
    }

    // Log after any controller method returns
    @AfterReturning(pointcut = "com.example.reward.aop.PointCuts.inControllerLayer()", returning = "result")
    public void logAfterRequest(JoinPoint joinPoint, Object result) {
        log.info("Response message: Method: {}() returned with value: {}", joinPoint.getSignature().getName(), result);
    }


    // Log after any controller method throws an exception
    @AfterThrowing(pointcut = "com.example.reward.aop.PointCuts.inControllerLayer()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception: Method: {} threw exception: {}", joinPoint.getSignature().getName(), exception.getMessage());
    }
}
