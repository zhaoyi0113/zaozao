package com.education.ws;


import com.education.db.DBConnection;
import com.education.db.entity.UserEntity;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/1/15.
 */
@Path("/login")
public class LoginService {

    private static final Logger logger = Logger.getLogger(LoginService.class.getName());

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
        if(WSUtility.whetherLogin(request)){
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
        Session currentSession = DBConnection.getCurrentSession();
        Query query = currentSession.createQuery("from UserEntity where userName='" + userName+"'");
        List<UserEntity> list = query.list();
        if(list.size()<=0){
            return false;
        }
        if(password.equals(list.get(0).getPassword())){
            return true;
        }
        return false;
    }


}
