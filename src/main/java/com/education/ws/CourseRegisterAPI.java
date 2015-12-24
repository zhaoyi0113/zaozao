package com.education.ws;

import com.education.db.entity.CourseEntity;
import com.education.db.jpa.CourseRepository;
import com.education.formbean.CourseQueryBean;
import com.education.service.CourseService;
import com.education.ws.util.WSUtility;
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
public class CourseRegisterAPI {

    private static final Logger logger = Logger.getLogger(CourseRegisterAPI.class.getName());
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


    @Path("create")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("multipart/mixed")
    public Response uploadFile(FormDataMultiPart multiPart) {
        CourseRegisterBean bean = new CourseRegisterBean();
        bean.setName(multiPart.getField("name").getValue());
        logger.info("create new course in category "+bean.getCategory());
        bean.setContent(multiPart.getField("content").getValue());
        bean.setIntroduction(multiPart.getField("introduction").getValue());
        bean.setTags(multiPart.getField("tags").getValue());
        bean.setPublishDate(multiPart.getField("publish_date").getValue());
        bean.setStatus(multiPart.getField("status").getValue());

        FormDataBodyPart multiPartFile = multiPart.getField("file");

        InputStream file = multiPartFile.getValueAs(InputStream.class);
        String imageDir = courseImagePath;
        String fileName = getFileNameFromMultipart(multiPartFile);
        logger.info("upload course " + bean.getName() + ", fileName=" + fileName);
        if (wsUtility.whetherVideo(fileName)) {
            bean.setVideoPath(fileName);
        } else {
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
            List<CourseQueryBean> allCoursesIndex = courseService.getAllCoursesIndex();
            return Response.ok().entity(allCoursesIndex).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("query")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryCourse(@QueryParam("category") String category, @QueryParam("history") String history) {
        List<CourseRegisterBean> list = null;
        if (history == null) {
            list = courseService.queryCourseByCategoryAfterNow(category);
        } else {
            list = courseService.queryCourseByCategoryBeforeNow(category);
        }
        System.out.println("query course " + list.size());
        return Response.ok(list).header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*").build();
    }

    @Path("/querycourse/{courseId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseById(@Context HttpServletRequest request,
                                  @PathParam("courseId") String courseId) {
        try {
            CourseQueryBean b = courseService.queryCourse(courseId);
            return Response.ok().entity(b).header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "*").build();
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
        return Response.ok(file).header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*")
                .header("Content-Disposition", "attachment; filename = " + fileName).build();
    }

    @Path("/findafter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCoursesAfterNow() {
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
        courseService.editCourse(bean);
        return Response.ok().build();
    }

    @Path("/upload_resource")
    @POST
    /**
     * edit course uses this api to upload updated course
     */
    public Response uploadResource(FormDataMultiPart multiPart) {
        String id = multiPart.getField("id").getValue();
        if (!courseRepository.exists(Integer.parseInt(id))) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Can't find course " + id).build();
        }
        CourseEntity course = courseRepository.findOne(Integer.parseInt(id));
        course.setCategory(Integer.parseInt(multiPart.getField("category").getValue()));
        course.setName(multiPart.getField("name").getValue());
        course.setContent(multiPart.getField("content").getValue());
        course.setYears(Integer.parseInt(multiPart.getField("months").getValue()));
        course.setTags(multiPart.getField("tags").getValue());

        FormDataBodyPart multiPartFile = multiPart.getField("file");
        InputStream file = multiPartFile.getValueAs(InputStream.class);
        String imageDir = courseImagePath;
        String fileName = getFileNameFromMultipart(multiPartFile);
        logger.info("upload file name " + fileName);

        if (wsUtility.whetherVideo(fileName)) {
            wsUtility.deleteFile(course.getVideoPath());
            course.setVideoPath(fileName);
        } else {
            wsUtility.deleteFile(course.getTitleImagePath());
            course.setTitleImagePath(fileName);
        }
        writeFile(file, imageDir, fileName);
        courseRepository.save(course);

        return Response.ok().build();
    }

    private String getFileNameFromMultipart(FormDataBodyPart multiPartFile) {
        String fileName = multiPartFile.getContentDisposition().getFileName();
        try {
            fileName = new String(fileName.getBytes("ISO-8859-1"),
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCourse(@PathParam("id") String id) {
        CourseEntity course = courseRepository.findOne(Integer.parseInt(id));
        courseRepository.delete(course);
        if (course.getTitleImagePath() != null) {
            String filePath = courseImagePath + "/" + course.getTitleImagePath();
            deleteFile(filePath);
        }
        return Response.ok().build();
    }

    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    private static void writeFile(InputStream input, String dir, String targetName) {
        try {
            System.out.println("write to file " + dir + "," + targetName);
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            FileOutputStream output = new FileOutputStream(dir + "/" + targetName);
            logger.info("save file in " + dir + "/" + targetName);
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


}
