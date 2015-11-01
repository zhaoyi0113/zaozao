package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.UserEntity;
import com.google.gson.GsonBuilder;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yzzhao on 11/1/15.
 */
@Path("/query")
public class UserQuery {

    @Path("/allusers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String queryAllUsers() {
        List<UserRegisterBean> allUsers = getAllUsers();
        GsonBuilder builder = new GsonBuilder();
        String json = builder.create().toJson(allUsers);
        return json;
    }

    public static List<UserRegisterBean> getAllUsers() {
        Session currentSession = DBConnection.getCurrentSession();
        Query query = currentSession.createQuery("from UserEntity");
        List list = query.list();
        List<UserRegisterBean> ret = new ArrayList<>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            UserEntity entity = (UserEntity) iterator.next();
            UserRegisterBean bean = getUserRegisterBean(entity);
            ret.add(bean);
        }
        return ret;
    }

    public static UserRegisterBean getUserByName(String userName) {
        Session currentSession = DBConnection.getCurrentSession();
        Query query = currentSession.createQuery("from UserEntity where userName ='" + userName + "'");
        List<UserEntity> list = query.list();
        if (list.size() <= 0) {
            return null;
        }
        return getUserRegisterBean(list.get(0));
    }

    private static UserRegisterBean getUserRegisterBean(UserEntity entity) {
        UserRegisterBean bean = new UserRegisterBean();
        bean.setAge(entity.getAge());
        bean.setUserName(entity.getUserName());
        bean.setPassword(entity.getPassword());
        bean.setGender(entity.getGender());
        bean.setUserId(entity.getUserId());
        bean.setPhone(entity.getPhone());
        bean.setEmail(entity.getEmail());
        DateFormat format = new SimpleDateFormat();
        try {
            bean.setBirthdate(format.format(entity.getBirthDate()));
        }catch(Exception e){

        }
        return bean;
    }

}
