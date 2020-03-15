package com.hairizma.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/image")
public interface ImageService {

    @GET
    @Path("/{id}{jpg:(.jpg)?}")
    @Produces("image/jpg")
    Response getImage(@PathParam("id") long id);

}
