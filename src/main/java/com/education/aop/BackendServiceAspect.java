package com.education.aop;

import com.education.db.entity.*;
import com.education.db.jpa.BackendLoginHistoryRepository;
import com.education.db.jpa.BackendUserRepository;
import com.education.db.jpa.BackendUserTrackingRepository;
import com.education.db.jpa.CourseRepository;
import com.education.ws.CourseRegisterBean;
import com.education.ws.util.ContextKeys;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
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

    @Autowired
    private BackendUserTrackingRepository trackingRepository;

    @Autowired
    private CourseRepository courseRepository;

    public BackendServiceAspect() {
        logger.info(this + " constructor");
    }

    @Pointcut("execution(* com.education.service.BackendLoginService.login(..))")
    public void backendUserLogin() {
    }

    @Pointcut("execution(* com.education.service.CourseProposalService.queryCourseByDate(..))")
    public void queryCourseByDate() {

    }

    @Pointcut("execution(* com.education.service.CourseService.createCourse(..))")
    public void backendUserCreateCourse() {
    }

    @Pointcut("execution(* com.education.service.CourseService.editCourse(..))")
    public void backendUserEditCourse() {
    }

    @Pointcut("execution(* com.education.service.CourseService.deleteCourse(..))")
    public void backendUserDeleteCourse() {
    }

    @Pointcut("execution(* com.education.service.CourseVideoService.setVideo(..))")
    public void backendUserUploadCourseVideo() {
    }

    @Pointcut("execution(* com.education.service.CourseTagService.addNewCourseTag(..))")
    public void backendUserCreateCourseTag() {
    }

    @Pointcut("execution(* com.education.service.CourseTagService.editCourseTag(..))")
    public void backendUserEditCourseTag() {
    }

    @Pointcut("execution(* com.education.service.CourseTagService.deleteCourseTag(..))")
    public void backendUserDeleteCourseTag() {
    }

    @AfterReturning(pointcut = "backendUserLogin()", returning = "retVal")
    public void afterLogin(JoinPoint joinPoint, Object retVal) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        logger.info("after login " + retVal + ", context=" + requestAttributes);

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

    @AfterReturning(pointcut = "backendUserCreateCourse()", returning = "courseId")
    @Transactional
    public void createCourse(JoinPoint joinPoint, int courseId) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof CourseRegisterBean) {
            CourseRegisterBean course = (CourseRegisterBean) args[0];
            String comments = " create course " + course.getName();
            saveUserTracking(comments);
        }
    }

    @AfterReturning(pointcut = "backendUserEditCourse()")
    @Transactional
    public void editCourse(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof CourseRegisterBean) {
            CourseRegisterBean course = (CourseRegisterBean) args[0];
            String comments = " edit course " + course.getName();
            saveUserTracking(comments);
        }
    }

    @AfterReturning(pointcut = "backendUserUploadCourseVideo()")
    @Transactional
    public void uploadCourseVideo(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof Integer) {
            int courseId = (int) args[0];
            CourseEntity course = courseRepository.findOne(courseId);
            if (course != null) {
                String comments = " upload video on course " + course.getName();
                saveUserTracking(comments);
            }
        }
    }

    @AfterReturning(pointcut = "backendUserDeleteCourse()", returning = "courseEntity")
    @Transactional
    public void deleteCourse(JoinPoint joinPoint, CourseEntity courseEntity) {
        if (courseEntity == null) {
            return;
        }
        String comments = " delete course " + courseEntity.getName();
        saveUserTracking(comments);
    }

    @AfterReturning(pointcut = "backendUserCreateCourseTag()")
    @Transactional
    public void createCourseTag(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof String) {
            String tagName = (String) args[0];
            String comments = " create course tag " + tagName;
            saveUserTracking(comments);
        }
    }

    @AfterReturning(pointcut = "backendUserEditCourseTag()")
    @Transactional
    public void editCourseTag(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 1 && args[1] instanceof String) {
            String tagName = (String) args[1];
            String comments = " edit course tag " + tagName;
            saveUserTracking(comments);
        }
    }

    @AfterReturning(pointcut = "backendUserDeleteCourseTag()", returning = "courseTagEntity")
    @Transactional
    public void deleteCourseTag(JoinPoint joinPoint, CourseTagEntity courseTagEntity) {
        if(courseTagEntity != null){
            String comments = " delete course tag "+courseTagEntity.getName();
            saveUserTracking(comments);
        }
    }

    private void saveUserTracking(String comments) {
        BackendUserEntity userEntity = getUserEntity();
        if (userEntity == null) {
            return;
        }
        comments = "User " + userEntity.getName() + comments;
        logger.info(comments);
        BackendUserTrackingEntity entity = new BackendUserTrackingEntity();
        entity.setTimeCreated(Calendar.getInstance().getTime());
        entity.setUserId(userEntity.getId());
        entity.setComments(comments);
        trackingRepository.save(entity);
    }

    private BackendUserEntity getUserEntity() {
        HttpSession session = getHttpSession();
        if (session != null) {
            String userName = (String) session.getAttribute(ContextKeys.SESSION_USER);
            List<BackendUserEntity> users = userRepository.findByName(userName);
            if (!users.isEmpty()) {
                return users.get(0);
            }
        }
        return null;
    }

    private HttpSession getHttpSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            return request.getSession();
        } catch (IllegalStateException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Before("queryCourseByDate()")
    public void beforeQueryCourse(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

    }

}
