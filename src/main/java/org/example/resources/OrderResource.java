package org.example.resources;


import org.example.controller.OrderController;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/order")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final OrderController orderController;

    public OrderResource(Jdbi jdbi){
        this.orderController = new OrderController(jdbi);
    }

    @GET
    public Response test(){
        return Response.ok().entity(orderController.test()).build();
    }

}
