package com.mercurio.lms.rest;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CustomContainerResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", requestContext.getHeaderString("Origin"));
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "True");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Accept, Access-Control-Allow-Methods, Access-Control-Allow-Headers, Authorization, Access-Control-Expose-Headers");
        responseContext.getHeaders().add("Access-Control-Expose-Headers", "*, Authorization");
    }
}
