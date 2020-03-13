package com.hairizma.service;

import com.hairizma.dto.Product;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/product")
@Produces(MediaType.APPLICATION_XML)
public interface ProductService {

    @GET
    @Path("/all")
    List<Product> getProducts();

}
