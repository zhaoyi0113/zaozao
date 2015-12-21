package com.education.ws;


import com.education.db.DBConnection;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserRepository;
import com.google.gson.Gson;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/1/15.
 */
@Path("/login")
@Service
public class LoginService {

    private static final Logger logger = Logger.getLogger(LoginService.class.getName());

    private LoginCheckService loginCheck;

    @Autowired
    private UserRepository userRepository;

    @POST
    public Response login(@Context HttpServletRequest request, @FormParam("userName") String userName, @FormParam("password") String password) {
        System.out.println("login " + userName);
        if(validateUser(userName, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user_name", userName);
            return Response.ok().entity("login success").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("login filed.").build();
    }

    @GET
    @Path("/check")
    public Response isLogin(@Context HttpServletRequest request){
        if(getLoginCheck().whetherLogin(request)){
            return Response.ok().entity("1").build();
        }
        return Response.ok().entity("0").build();
    }

    @GET
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();;
        return Response.ok().build();
    }

    public boolean validateUser(String userName, String password){
//        Session currentSession = DBConnection.getCurrentSession();
//        Query query = currentSession.createQuery("from UserEntity where userName='" + userName+"'");
//        List<UserEntity> list = query.list();

        Iterable<UserEntity> iterable = userRepository.findAll();//ByUserNameAndPassword(userName, password);
        for(Iterator<UserEntity> iter = iterable.iterator(); iter.hasNext();){
            UserEntity entity = iter.next();
        }
        return false;
    }

    public LoginCheckService getLoginCheck() {
        return loginCheck;
    }

    @Autowired(required = true)
    public void setLoginCheck(LoginCheckService loginCheck) {
        this.loginCheck = loginCheck;
    }

    public static String buildNotLoginJson(){
        Map<String, String> entity = new Hashtable<>();
        entity.put("status", "" + ResponseStatus.NOT_LOGIN.value());
        entity.put("message", "not login");
        Gson gson = new Gson();
        String json = gson.toJson(entity);
        return json;
    }
}
