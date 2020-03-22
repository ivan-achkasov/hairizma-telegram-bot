package com.hairizma.service;

import com.hairizma.dto.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

@Path("/product")
@Produces(MediaType.APPLICATION_XML)
public interface ProductService {

    @GET
    @Path("/all")
    List<Product> getProducts();

    @GET
    @Path("/byIds")
    List<Product> getProducts(@QueryParam("ids") Set<Integer> ids);

    @GET
    @Path("/{id}")
    Product get(@PathParam("id") int id);

}
