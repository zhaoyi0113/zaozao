package com.education.aop;

import com.education.db.entity.BackendLoginHistoryEntity;
import com.education.db.entity.BackendUserEntity;
import com.education.db.jpa.BackendLoginHistoryRepository;
import com.education.db.jpa.BackendUserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/2/16.
 */
@Aspect
@Component
public class BackendServiceAspect {

    private static final Logger logger = Logger.getLogger(BackendServiceAspect.class.getName());


    @Autowired
    private BackendLoginHistoryRepository loginHistoryRepository;

    @Autowired
    private BackendUserRepository userRepository;

    public BackendServiceAspect() {
        logger.info(this + " constructor");
    }

    @Pointcut("execution(* com.education.service.BackendLoginService.login(..))")
    public void backendUserLogin() {
    }

//    @Before("backendUserLogin()")
//    public void before(JoinPoint joinPoint) {
//        logger.info("before");
//        Object[] args = joinPoint.getArgs();
//        for (Object obj : args){
//            logger.info("parameters:"+ obj);
//        }
//    }

    @AfterReturning(pointcut = "backendUserLogin()", returning = "retVal")
    public void afterLogin(JoinPoint joinPoint, Object retVal) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        logger.info("after login " + retVal+", context="+ requestAttributes);

        String userName = (String) joinPoint.getArgs()[0];
        if (retVal instanceof Boolean) {
            Boolean login = (Boolean) retVal;
            List<BackendUserEntity> userNameList = userRepository.findByName(userName);
            if (!userNameList.isEmpty()) {
                BackendLoginHistoryEntity entity = new BackendLoginHistoryEntity();
                entity.setLoginStatus(login ? 1 : 0);
                entity.setLoginTime(Calendar.getInstance().getTime());
                entity.setUserId(userNameList.get(0).getId());
                loginHistoryRepository.save(entity);
            }

        }
    }


}
