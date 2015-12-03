package com.education.ws;

import com.google.gson.Gson;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

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
@Path("/fileupload")
public class CourseUploaderService {

    private static final Logger logger = Logger.getLogger(CourseUploaderService.class.getName());

    @PUT
    @Path("/manager")
    public Response controller(){
        System.out.println("controller");
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadImage( FormDataMultiPart multiPart,
                                 @FormDataParam("imgFile") FormDataContentDisposition disposition,
                                 @QueryParam("dir") String type
                                ){
        System.out.println("upload image type "+type);
        Map<String, List<FormDataBodyPart>> fields = multiPart.getFields();
        for(Map.Entry<String, List<FormDataBodyPart>> entry : fields.entrySet()){
            System.out.println(entry.getKey()+"="+entry.getValue());
        }
        System.out.println("filename="+disposition.getFileName());
        InputStream input = multiPart.getField("imgFile").getValueAs(InputStream.class);

        File tmpDir = new File(CourseRegisterService.WEBAPP_PUBLIC_RESOURCES_COURSES+"/tmp");
        tmpDir.mkdirs();
        File imageFile = new File(tmpDir.getPath()+"/"+System.currentTimeMillis()+"_"+disposition.getFileName());

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
        Map<String, Object> resp = new Hashtable<>();
        resp.put("url", "public/resources/courses/tmp/"+imageFile.getName());
        resp.put("error", 0);
        Gson gson = new Gson();
        String json = gson.toJson(resp);
        System.out.println("resp:"+json);
        return Response.ok(resp).build();
    }
}
