package com.education.filter;

import com.education.exception.BadRequestException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by yzzhao on 1/21/16.
 */
@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException>{

    @Override
    public Response toResponse(BadRequestException exception) {

        return Response.status(exception.getErrorCode().getCode()).entity(exception.getMessage()).build();
    }
}
