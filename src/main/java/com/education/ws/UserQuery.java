package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.UserEntity;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.service.BackendLoginService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/1/15.
 */
@Path("/query")
public class UserQuery {

    private static final Logger logger = Logger.getLogger(UserQuery.class.getName());

    private BackendLoginService loginCheck;

    @Path("/allusers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String queryAllUsers(@Context HttpServletRequest request) {
        if(!getLoginCheck().whetherLogin(request)){
            Gson gson = new Gson();
            return gson.toJson("Not Login");
        }
        try {
            List<UserChildrenRegisterBean> allUsers = getAllUsers();
            GsonBuilder builder = new GsonBuilder();
            String json = builder.create().toJson(allUsers);
            System.out.println("get all users "+json);
            logger.info("get all users " + json);
            return json;
        }finally{
            DBConnection.closeSession();
        }
    }

    public static List<UserChildrenRegisterBean> getAllUsers() {
        Session currentSession = DBConnection.getCurrentSession();
        Query query = currentSession.createQuery("from UserEntity");
        List list = query.list();
        List<UserChildrenRegisterBean> ret = new ArrayList<>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            UserEntity entity = (UserEntity) iterator.next();
//            UserChildrenRegisterBean bean = getUserRegisterBean(entity);
//            ret.add(bean);
        }
        return ret;
    }


    public static UserEntity getUserEntityByName(String userName){
        Session currentSession = DBConnection.getCurrentSession();
        Query query = currentSession.createQuery("from UserEntity where userName ='" + userName + "'");
        List<UserEntity> list = query.list();
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    public static UserEntity getUserEntityById(int userId){
        Session currentSession = DBConnection.getCurrentSession();
        Query query = currentSession.createQuery("from UserEntity where user_id =" + userId);
        List<UserEntity> list = query.list();
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }


    public BackendLoginService getLoginCheck() {
        return loginCheck;
    }

    @Autowired(required = true)
    public void setLoginCheck(BackendLoginService loginCheck) {
        this.loginCheck = loginCheck;
    }
}
