package com._98point6.droptoken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 */
public class DropTokenExceptionMapper implements ExceptionMapper<Exception>  {
    private static final Logger logger = LoggerFactory.getLogger(DropTokenExceptionMapper.class);
//    public Response toResponse(RuntimeException e) {
//        logger.error("Unhandled exception.", e);
//        return Response.status(500).build();
//    }
    @Override
    public Response toResponse(Exception e) {
        logger.error("Unhandled exception.", e);
        Response.Status responseCode = Response.Status.INTERNAL_SERVER_ERROR;
        String message = e.getMessage();
        if (e instanceof NotFoundException) {
            responseCode = Response.Status.NOT_FOUND;
            message = e.getMessage();
        } else if (e instanceof BadRequestException) {
            responseCode = Response.Status.BAD_REQUEST;
//            message = e.getMessage();
            message = "Malformed input. Illegal move";
        } else if (e instanceof NotAllowedException) {
            responseCode = Response.Status.METHOD_NOT_ALLOWED;
            message = e.getMessage();
        } else if (e instanceof WebApplicationException) {
            WebApplicationException realException = (WebApplicationException) e;
            int response = realException.getResponse().getStatus();
            responseCode = Response.Status.fromStatusCode(response);
        }
        return Response.status(responseCode.getStatusCode())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(message)
                .build();
    }
}
