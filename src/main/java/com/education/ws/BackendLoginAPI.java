package com.education.ws;


import com.education.db.jpa.UserRepository;
import com.education.service.BackendLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/1/15.
 */
@Path("/backend_login")
@Service
public class BackendLoginAPI {

    private static final Logger logger = Logger.getLogger(BackendLoginAPI.class.getName());

    @Autowired
    private BackendLoginService loginService;

    @POST
    public Response login(@Context HttpServletRequest request, @FormParam("userName") String userName, @FormParam("password") String password) {
        logger.info("login " + userName);
        if (loginService.login(userName, password)) {
            HttpSession session = request.getSession();
            logger.info("session interval:" + session.getMaxInactiveInterval());
            session.setAttribute("user_name", userName);
            return Response.ok().entity(loginService.getUserRole(userName, password)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("login filed.").build();
    }

    @GET
    @Path("/check")
    public Response isLogin(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("user_name");
        if (userName != null) {
            String userRole = loginService.getUserRole(userName);
            return Response.ok(userRole).build();
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


}
