package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.CourseEntity;
import com.education.db.jpa.CourseRepository;
import com.google.gson.Gson;
import jersey.repackaged.com.google.common.collect.Lists;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/3/15.
 */
@Path("course")
@EnableTransactionManagement
@Transactional
public class CourseRegisterService {

    private static final Logger logger = Logger.getLogger(CourseRegisterService.class.getName());
    public static final String WEBAPP_PUBLIC_RESOURCES_COURSES = "src/main/webapp/public/resources/courses/";

    private LoginCheckService loginCheck;

    @Autowired
    private CourseRepository courseRepository;

    @Path("new")
    @POST
    public Response createNewCourse(@BeanParam CourseRegisterBean bean) {
        System.out.println("create new course " + bean);
        logger.info("create new course " + bean);
        try {
            createCourse(bean);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } finally {
            DBConnection.closeSession();
        }
        return Response.ok().build();
    }

    @Path("uploadfile")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("multipart/mixed")
    public Response uploadFile(FormDataMultiPart multiPart) {
        CourseRegisterBean bean = new CourseRegisterBean();
        bean.setName(multiPart.getField("name").getValue());
        bean.setCategory(multiPart.getField("category").getValue());
        bean.setDate(multiPart.getField("date").getValue());
        System.out.println("get date "+bean.getDate());
        bean.setContent(multiPart.getField("content").getValue());
        bean.setPicturePaths(bean.getName());
        int id = createNewCourseWithBean(bean);
        InputStream file = multiPart.getField("file").getValueAs(InputStream.class);
        String imageDir = System.getProperty("COURSE_IMAGE_DIR");
        if (imageDir == null) {
            imageDir = WEBAPP_PUBLIC_RESOURCES_COURSES;
        }
        imageDir += "/" + id;
        writeFile(file, imageDir, bean.getName());
        return Response.ok().build();
    }

    @Path("queryall")
    @GET
    public Response getAllCourses() {
        try {
            Iterable<CourseEntity> courseIterable = courseRepository.findAll();
            List<CourseEntity> allCourse = Lists.newArrayList(courseIterable); //getCourseDao().getAllCourses();
            Gson gson = new Gson();
            String json = gson.toJson(allCourse);
            return Response.ok().entity(json).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/querycourse/{courseId}")
    @GET
    public Response getCourseById(@Context HttpServletRequest request,
                                  @PathParam("courseId") String courseId) {
        if (!loginCheck.whetherLogin(request)) {
            String json = LoginService.buildNotLoginJson();
            return Response.status(Response.Status.BAD_REQUEST).entity(json).build();
        }
        try {
            int id = Integer.parseInt(courseId);

            CourseEntity course =courseRepository.findOne(id);// getCourseDao().getCourseById(id);
            String courseResourcePath = findCourseResourcesPath(course.getId());
            String json = buildCourseJsonData(course, courseResourcePath);
            return Response.ok().entity(json).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } finally {
            DBConnection.closeSession();
        }
    }

    @Path("/findafter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCoursesAfterNow(){
        Date now = Calendar.getInstance().getTime();
        List<CourseEntity> courses = courseRepository.findByDateAfter(now);
        return Response.ok(courses).build();
    }

    private String buildCourseJsonData(CourseEntity course, String courseResourcePath) {
        if (courseResourcePath != null) {
            File dir = new File(courseResourcePath);
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                course.setPicture_paths(files[0].getName());
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(course);
        return json;
    }

    @Path("query/allnames")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCourseNames() {
        try {
            List<String> names = courseRepository.findAllCourseNames();
            return Response.ok().entity(names).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/edit")
    @POST
    public Response editCourse(@Context HttpServletRequest request, @BeanParam CourseRegisterBean bean) {
        if (!loginCheck.whetherLogin(request)) {
            String json = LoginService.buildNotLoginJson();
            return Response.status(Response.Status.BAD_REQUEST).entity(json).build();
        }
        CourseEntity one = courseRepository.findOne(Integer.parseInt(bean.getId()));
        one.setContent(bean.getContent());
        one.setCategory(bean.getCategory());
        SimpleDateFormat format = WSUtility.getDateFormat();
        try {
            System.out.println("get date "+bean.getDate());
            one.setDate(format.parse(bean.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        courseRepository.save(one);
        return Response.ok().build();
    }

    @Path("/upload_resource")
    @POST
    public Response uploadResource(FormDataMultiPart multiPart){
        String id = multiPart.getField("id").getValue();
        if(!courseRepository.exists(Integer.parseInt(id))){
            return Response.status(Response.Status.BAD_REQUEST).entity("Can't find course "+id).build();
        }
        CourseEntity course = courseRepository.findOne(Integer.parseInt(id));
        InputStream file = multiPart.getField("file").getValueAs(InputStream.class);
        String imageDir = System.getProperty("COURSE_IMAGE_DIR");
        if (imageDir == null) {
            imageDir = WEBAPP_PUBLIC_RESOURCES_COURSES;
        }
        imageDir += "/" + id;
        writeFile(file, imageDir, course.getName());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCourse(@PathParam("id") String id){
        CourseEntity course = courseRepository.findOne(Integer.parseInt(id));
        courseRepository.delete(course);
        return Response.ok().build();
    }

    private int createCourse(CourseRegisterBean bean) {
        CourseEntity entity = new CourseEntity(bean);
        CourseEntity save = courseRepository.save(entity);
        return save.getId();
    }

    private int createNewCourseWithBean(CourseRegisterBean bean) {
        System.out.println("create new course " + bean);
        logger.info("create new course " + bean);
        try {
            int id = createCourse(bean);
            return id;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DBConnection.closeSession();
        }
        return -1;
    }

    private static void writeFile(InputStream input, String dir, String targetName) {
        try {
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            FileOutputStream output = new FileOutputStream(dir + "/" + targetName);
            byte buffer[] = new byte[512];
            int read = input.read(buffer);
            while (read > 0) {
                output.write(buffer, 0, read);
                read = input.read(buffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String findCourseResourcesPath(int courseId) {
        String imageDir = System.getProperty("COURSE_IMAGE_DIR");
        if (imageDir == null) {
            imageDir = WEBAPP_PUBLIC_RESOURCES_COURSES;
        }
        File dir = new File(imageDir + "/" + courseId);
        if (dir.exists()) {
            return dir.getPath();
        }
        return null;
    }


    public LoginCheckService getLoginCheck() {
        return loginCheck;
    }

    @Autowired(required = true)
    public void setLoginCheck(LoginCheckService loginCheck) {
        this.loginCheck = loginCheck;
    }

}
