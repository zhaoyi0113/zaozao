package com.education.ws;

import com.education.db.entity.CourseEntity;
import com.education.db.jpa.CourseRepository;
import com.education.service.CourseService;
import com.education.ws.util.WSUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jersey.repackaged.com.google.common.collect.Lists;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
@Service
public class CourseRegisterService {

    private static final Logger logger = Logger.getLogger(CourseRegisterService.class.getName());
    public static final String WEBAPP_PUBLIC_RESOURCES_COURSES = "src/main/webapp/public/resources/courses/";

    private LoginCheckService loginCheck;

    @Autowired
    private CourseRepository courseRepository;

    @Value("#{config['course_image_path']}")
    private String courseImagePath;

    @Value("#{config['course_image_url']}")
    private String courseImageUrl;

    @Autowired
    private WSUtility wsUtility;

    @Autowired
    private CourseService courseService;

//    @Path("new")
//    @POST
//    public Response createNewCourse(@BeanParam CourseRegisterBean bean) {
//        System.out.println("create new course " + bean);
//        logger.info("create new course " + bean);
//        try {
//            createCourse(bean);
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage(), e);
//            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
//        }
//        return Response.ok().build();
//    }

    @Path("create")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("multipart/mixed")
    public Response uploadFile(FormDataMultiPart multiPart) {
        CourseRegisterBean bean = new CourseRegisterBean();
        bean.setName(multiPart.getField("name").getValue());
        bean.setCategory(multiPart.getField("category").getValue());
        bean.setDate(multiPart.getField("date").getValue());
        bean.setContent(multiPart.getField("content").getValue());
        bean.setIntroduction(multiPart.getField("introduction").getValue());

        FormDataBodyPart multiPartFile = multiPart.getField("file");

        InputStream file = multiPartFile.getValueAs(InputStream.class);
        String imageDir = courseImagePath;
        String fileName = multiPartFile.getContentDisposition().getFileName();
        try {
            fileName = new String(fileName.getBytes("ISO-8859-1"),
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("upload course "+bean.getName()+", fileName="+fileName);
        if(wsUtility.whetherVideo(fileName)){
            bean.setVideoPath(fileName);
        }else{
            bean.setTitleImagePath(fileName);
        }
        courseService.createCourse(bean);
        writeFile(file, imageDir, fileName);



        return Response.ok().build();
    }

    @Path("queryall")
    @GET
    public Response getAllCourses() {
        try {
            Iterable<CourseEntity> courseIterable = courseRepository.findAll();
            List<CourseEntity> allCourse = Lists.newArrayList(courseIterable); //getCourseDao().getAllCourses();
            Gson gson = new GsonBuilder().setDateFormat(wsUtility.getDateFormatString()).create();
            String json = gson.toJson(allCourse);
            return Response.ok().entity(json).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("query")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryCourse(@QueryParam("category") String category, @QueryParam("history") String history){
        List<CourseRegisterBean> list = null;
        if(history == null){
            list = courseService.queryCourseByCategoryAfterNow(category);
        }else {
            list = courseService.queryCourseByCategoryBeforeNow(category);
        }
        System.out.println("query course "+list.size());
        return Response.ok(list).header("Access-Control-Allow-Origin","*")
                .header("Access-Control-Allow-Methods","*").build();
    }

    @Path("/querycourse/{courseId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseById(@Context HttpServletRequest request,
                                  @PathParam("courseId") String courseId) {
        try {
            int id = Integer.parseInt(courseId);

            CourseEntity course =courseRepository.findOne(id);// getCourseDao().getCourseById(id);
            if(course == null){
                throw new BadRequestException("can't find course "+courseId);
            }
            CourseRegisterBean bean = new CourseRegisterBean(course, wsUtility);
            Gson gson = new GsonBuilder().setDateFormat(wsUtility.getDateFormatString()).create();
            String json = gson.toJson(bean);
            return Response.ok().entity(json).header("Access-Control-Allow-Origin","*")
                    .header("Access-Control-Allow-Methods","*").build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/file/{courseId}/{filename}")
    @GET
    public Response getCourseVideoById(@Context HttpServletRequest request,
                                  @PathParam("courseId") String courseId,
                                       @PathParam("filename") String fileName) {
        FileInputStream file = courseService.getCourseFile(courseId, fileName);
        return Response.ok(file).header("Access-Control-Allow-Origin","*")
                .header("Access-Control-Allow-Methods","*")
                .header("Content-Disposition", "attachment; filename = "+fileName).build();
    }

    @Path("/findafter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCoursesAfterNow(){
        Date now = Calendar.getInstance().getTime();
        List<CourseEntity> courses = courseRepository.findByDateAfter(now);
        return Response.ok(courses).build();
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
    /**
     * when update course without image/video, use this api
     */
    public Response editCourse(@Context HttpServletRequest request, @BeanParam CourseRegisterBean bean) {
        CourseEntity one = courseRepository.findOne(Integer.parseInt(bean.getId()));
        one.setContent(bean.getContent());
        one.setCategory(bean.getCategory());
        one.setIntroduction(bean.getIntroduction());
        SimpleDateFormat format = wsUtility.getDateFormat();
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
    /**
     * edit course uses this api to upload updated course
     */
    public Response uploadResource(FormDataMultiPart multiPart){
        String id = multiPart.getField("id").getValue();
        if(!courseRepository.exists(Integer.parseInt(id))){
            return Response.status(Response.Status.BAD_REQUEST).entity("Can't find course "+id).build();
        }
        CourseEntity course = courseRepository.findOne(Integer.parseInt(id));
        course.setCategory(multiPart.getField("category").getValue());
        course.setName(multiPart.getField("name").getValue());
        course.setContent(multiPart.getField("content").getValue());
        try{
            SimpleDateFormat format = wsUtility.getDateFormat();
            Date date = format.parse(multiPart.getField("date").getValue());
            format.format(date);
            course.setDate(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        FormDataBodyPart multiPartFile = multiPart.getField("file");
        InputStream file = multiPartFile.getValueAs(InputStream.class);
        String imageDir = courseImagePath;
        String fileName = multiPartFile.getContentDisposition().getFileName();
        try {
            fileName = new String(fileName.getBytes("ISO-8859-1"),
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("upload file name "+fileName);

        if(wsUtility.whetherVideo(fileName)){
            wsUtility.deleteFile(course.getVideoPath());
            course.setVideoPath(fileName);
        }else {
            wsUtility.deleteFile(course.getTitleImagePath());
            course.setTitleImagePath(fileName);
        }
        writeFile(file, imageDir, fileName);
        courseRepository.save(course);

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCourse(@PathParam("id") String id){
        CourseEntity course = courseRepository.findOne(Integer.parseInt(id));
        courseRepository.delete(course);
        if(course.getTitleImagePath() != null) {
            String filePath = courseImagePath + "/" + course.getTitleImagePath();
            deleteFile(filePath);
        }
        return Response.ok().build();
    }

    private static void deleteFile(String filePath){
        File file = new File(filePath);
        file.delete();
    }

    private static void writeFile(InputStream input, String dir, String targetName) {
        try {
            System.out.println("write to file "+dir+","+targetName);
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            FileOutputStream output = new FileOutputStream(dir + "/" + targetName);
            logger.info("save file in "+dir+"/"+targetName);
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

    private String updateCourseTitleImagePath(CourseEntity course){
        return (this.courseImageUrl+"/"+course.getTitleImagePath());
    }

}
