package com.education.ws;


import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserRepository;
import com.education.service.BackendLoginService;
import com.google.gson.Gson;
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
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/1/15.
 */
@Path("/backend_login")
@Service
public class BackendLoginAPI {

    private static final Logger logger = Logger.getLogger(BackendLoginAPI.class.getName());

    private BackendLoginService loginService;

    @Autowired
    private UserRepository userRepository;

    @POST
    public Response login(@Context HttpServletRequest request, @FormParam("userName") String userName, @FormParam("password") String password) {
        logger.info("login " + userName);
        if (loginService.login(userName, password)) {
            HttpSession session = request.getSession();
            logger.info("session interval:"+session.getMaxInactiveInterval());
            session.setAttribute("user_name", userName);
            return Response.ok().entity(loginService.getUserRole(userName, password)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("login filed.").build();
    }

    @GET
    @Path("/check")
    public Response isLogin(@Context HttpServletRequest request) {
        if (loginService.whetherLogin(request)) {
            logger.info("logined in");
            return Response.ok().entity("1").build();
        }
        logger.info("not login");
        return Response.ok().entity("0").build();
    }

    @GET
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return Response.ok().build();
    }

    @Autowired(required = true)
    public void setLoginCheck(BackendLoginService loginCheck) {
        this.loginService = loginCheck;
    }

    public static String buildNotLoginJson() {
        Map<String, String> entity = new Hashtable<>();
        entity.put("status", "" + ResponseStatus.NOT_LOGIN.value());
        entity.put("message", "not login");
        Gson gson = new Gson();
        String json = gson.toJson(entity);
        return json;
    }
}
