package com.uniza.mr.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    public Response toResponse(Throwable exception) {

        //Default JAX-RS exception
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        String msg = "";
        msg = exception.getMessage();
        if ( msg == null) {
            msg = exception.getClass().getName();
        }

        msg = "E;999;" + msg;

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .header("message", msg)
                .build();
    }

}
