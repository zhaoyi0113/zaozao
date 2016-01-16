package com.education.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by yzzhao on 1/16/16.
 */
@Path("/demo")
public class DemoAPI {

    @GET
    public InputStream getImageFile(){
        try {
            return new FileInputStream("/Users/yzzhao/Downloads/1111.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
