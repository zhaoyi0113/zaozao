package com.education.aop;

import com.education.service.UserCourseHistoryService;
import com.education.service.WeChatUserInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/9/16.
 */

@Aspect
@Component
public class WeChatUserAspect {

    private static final Logger logger = Logger.getLogger(WeChatUserAspect.class.getName());

    @Autowired
    private UserCourseHistoryService historyService;

    @Pointcut("execution(* com.education.service.CourseProposalService.queryCourse(..))")
    public void queryCourse() {

    }

    @Before("queryCourse()")
    @Transactional
    public void beforeQueryCourse(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length >= 2) {
            WeChatUserInfo userInfo = (WeChatUserInfo) args[0];
            int courseId = (int) args[1];
            logger.info("query course aop " + courseId);
            if (userInfo != null) {
                historyService.saveUserAccessHistory(userInfo, courseId);
            }else{
                historyService.saveGuestAccessHistory(courseId);
            }
        } else {
            logger.severe("can't get query course parameters");
        }
    }


}
