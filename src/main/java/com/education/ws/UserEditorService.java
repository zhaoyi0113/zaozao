package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.UserEntity;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/1/15.
 */
@Path("/editor")
public class UserEditorService {

    private static final Logger logger = Logger.getLogger(UserEditorService.class.getName());

    @POST
    @Path("user")
    @Produces(MediaType.TEXT_PLAIN)
//    @Consumes(MediaType.TEXT_PLAIN)
    public Response editUser(@BeanParam UserEditBean bean) {
        System.out.println("user edit bean " + bean);
        UserEntity user = null;
        try {
            user = UserQuery.getUserEntityById(bean.getUserId());
        }finally {
            DBConnection.closeSession();
        }
        if (user == null) {
            logger.severe("can't fine user " + bean.getUserName());
            return Response.status(Response.Status.BAD_REQUEST).entity("user not exist.").build();
        }
        try {
            updateUser(bean, user);
            return Response.ok().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } finally {
            DBConnection.closeSession();
        }

    }

    public static void updateUser(UserEditBean user, UserEntity currentUser) {
        Session currentSession = DBConnection.getCurrentSession();
        if (user.getBirthdate() != null) {
        }
        if (user.getGender() != null) {
            currentUser.setGender(user.getGender());
        }
        if (user.getPhone() != null) {
            currentUser.setPhone(user.getPhone());
        }
        if (user.getAge() != 0) {
            currentUser.setAge(user.getAge());
        }
        if (user.getUserName() != null) {
            currentUser.setUserName(user.getUserName());
        }
        if (user.getEmail() != null) {
            currentUser.setEmail(user.getEmail());
        }
        currentSession.beginTransaction();
        currentSession.update(currentUser);
        currentSession.getTransaction().commit();
    }
}
