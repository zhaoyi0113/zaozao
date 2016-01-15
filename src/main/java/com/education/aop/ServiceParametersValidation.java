package com.education.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/15/16.
 */
@Aspect
@Component
public class ServiceParametersValidation {

    private static final Logger logger = Logger.getLogger(ServiceParametersValidation.class.getName());

}
