package com.example.reward.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointCuts {

    @Pointcut("within(com.example.reward.controller.*)")
    public void inControllerLayer(){}



}
