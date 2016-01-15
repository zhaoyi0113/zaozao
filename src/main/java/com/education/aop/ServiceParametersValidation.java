package com.education.aop;

import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.util.MoveAction;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/15/16.
 */
@Aspect
@Component
public class ServiceParametersValidation {

    private static final Logger logger = Logger.getLogger(ServiceParametersValidation.class.getName());

    @Pointcut("execution(* com.education.service.HomeConfigService.moveItem(..))")
    public void validateMoveItem() {
    }

    @Before("validateMoveItem()")
    public void validateHomeConfigMoveItem(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String action = (String) args[1];
        try {
            MoveAction.valueOf(action);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new BadRequestException(ErrorCode.ILLEGAL_REQUEST);
        }
    }

}
