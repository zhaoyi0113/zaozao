package com.education.ws;

import com.google.gson.Gson;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/22/15.
 */
@Path("/bower_components")
public class CourseUploaderService {

    private static final Logger logger = Logger.getLogger(CourseUploaderService.class.getName());

    @Path("/ueditor-bower/controller")
    @GET
    public Response controller(@QueryParam("action") String action, @QueryParam("noCache") String noCache){
        System.out.println("upload resource "+action+", noCache:"+noCache);
        Gson gson = new Gson();
        InputStream inputStream = getClass().getResourceAsStream("/editor/config.json");
        Map json = gson.fromJson(new InputStreamReader(inputStream), Map.class);

        return Response.ok(gson.toJson(json)).build();
    }

    @Path("/ueditor-bower/controller")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImage(@QueryParam("action") String action, FormDataMultiPart multiPart){
        System.out.println("action "+action);
        Map<String, List<FormDataBodyPart>> fields = multiPart.getFields();
        for(Map.Entry<String, List<FormDataBodyPart>> entry : fields.entrySet()){
            System.out.println(entry.getKey());
        }
        System.out.println("filename="+multiPart.getField("filename"));
        System.out.println("file="+multiPart.getField("file"));
        System.out.println("upfile="+multiPart.getField("upfile"));
        InputStream input = multiPart.getField("upfile").getValueAs(InputStream.class);

        File tmpDir = new File(CourseRegisterService.WEBAPP_PUBLIC_RESOURCES_COURSES+"/tmp");
        tmpDir.mkdirs();
        File imageFile = new File(tmpDir.getPath()+"/"+System.currentTimeMillis());

        byte buffer[] = new byte[512];
        int read = 0;
        try {
            FileOutputStream output = new FileOutputStream(imageFile);
            while((read = input.read(buffer)) > 0){
                output.write(buffer, 0 , read);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> resp = new Hashtable<>();
        resp.put("original", imageFile.getName());
        resp.put("name", imageFile.getName());
        resp.put("url", "public/resources/courses/tmp/"+imageFile.getName());
        resp.put("size", read+"");
        resp.put("type", "jpg");
        resp.put("state", "SUCCESS");
        Gson gson = new Gson();
        String json = gson.toJson(resp);
        System.out.println("resp:"+json);
        return Response.ok(json).build();
    }
}
