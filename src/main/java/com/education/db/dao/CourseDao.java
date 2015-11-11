package com.education.db.dao;

import com.education.db.DBConnection;
import com.education.db.entity.CourseEntity;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yzzhao on 11/9/15.
 */
@Repository
@Transactional
public class CourseDao {

    //    @Autowired  //自动注入JdbcTemplate的Bean
    private HibernateTemplate hibernateTemplate;

    private SessionFactory sessionFactory;

    public List<CourseEntity> getAllCourses() {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            Query query = session.createQuery("from CourseEntity");
            List<CourseEntity> courses = query.list();
            return courses;
        } finally {
//            getSessionFactory().getCurrentSession().close();
            if (session != null) {
                session.close();
            }
        }
    }

    public CourseEntity getCourseById(int id) {
        Session currentSession = null;
        try {
            currentSession = getSessionFactory().openSession();
            Query query = currentSession.createQuery("from CourseEntity where id=" + id);
            List<CourseEntity> list = query.list();
            if (list != null && !list.isEmpty()) {
                CourseEntity course = list.get(0);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    Date date = format.parse(course.getDate());
                    course.setDate(format.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();

                }
                return list.get(0);
            }
        } finally {
//            getSessionFactory().getCurrentSession().close();
            if (currentSession != null) {
                currentSession.close();
            }
        }
        return null;
    }

    public List<String> getAllCourseNames() {
        Session session = getSessionFactory().openSession();
        try {
            Query query = session.createQuery("select name from CourseEntity");
            return query.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void editCourse(CourseEntity entity) {
        Session session = getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from CourseEntity where id=" + entity.getId());
            if(query.list().size()<=0){
                return;
            }
            session.clear();
//            entity = (CourseEntity)query.list().get(0);
            System.out.println("update course entity "+entity.getContent());
            session.update(entity);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean whetherCourseExist(String id) {
        Session session = getSessionFactory().openSession();
        try {
            Query query = session.createQuery("from CourseEntity where id=" + id);
            List list = query.list();
            return list == null ? false : list.size() > 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
//        hibernateTemplate = new HibernateTemplate(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

}
