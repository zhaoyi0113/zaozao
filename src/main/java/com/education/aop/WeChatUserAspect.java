package com.education.aop;

import com.education.db.entity.COURSE_ACCESS_FLAG;
import com.education.db.entity.UserEntity;
import com.education.service.UserCourseHistoryService;
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
            UserEntity userInfo = (UserEntity) args[0];
            int courseId = (int) args[1];
            logger.info("query course aop " + courseId);
            if (userInfo != null) {
                historyService.saveUserAccessHistory(userInfo, courseId, COURSE_ACCESS_FLAG.VIEW);
            }else{
                historyService.saveUserAccessHistory(null, courseId, COURSE_ACCESS_FLAG.GUEST);
            }
        } else {
            logger.severe("can't get query course parameters");
        }
    }


}
