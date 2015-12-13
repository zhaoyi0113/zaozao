package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.jpa.CourseRepository;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("ResourceService")
public class ResourceService {

    private static final Logger logger = Logger.getLogger(ResourceService.class.getName());

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WSUtility wsUtility;

    public InputStream getResource(String id, String fileName) {
        CourseEntity entity = courseRepository.findOne(Integer.parseInt(id));
        if (entity == null) {
            throw new BadRequestException("can't find course id=" + id);
        }
        String path = wsUtility.getResourcePhysicalPath(fileName);
        try {
            FileInputStream input = new FileInputStream(path);
            return input;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
