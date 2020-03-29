package com.hairizma.service;

import com.hairizma.dto.CustomerDeliveryInfo;

import javax.ws.rs.*;

@Path("/customer")
public interface CustomerService {

    @GET
    @Path("/{id}")
    CustomerDeliveryInfo getCustomerInfo(@PathParam("id") long chatId);

    @GET
    @Path("/{id}/getOrCreate")
    CustomerDeliveryInfo getOrCreateCustomerInfo(@PathParam("id") long chatId);

    @POST
    @Path("/{id}/save")
    void saveCustomerInfo(CustomerDeliveryInfo customerDeliveryInfo);

}
