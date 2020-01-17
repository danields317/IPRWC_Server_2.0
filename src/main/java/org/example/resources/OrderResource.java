package org.example.resources;

import org.example.controller.OrderController;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.jdbi.v3.core.Jdbi;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Path("/api/order")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final OrderController orderController;

    public OrderResource(Jdbi jdbi){
        this.orderController = new OrderController(jdbi);
    }

    @POST
    public Response placeOrder(Order order){
        try {
            orderController.placeOrder(order);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NullPointerException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing necessary parameters").build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to fulfill request due to unforeseen circumstances").build();
        }
    }

    @PUT
    @RolesAllowed("Admin")
    public Response updateOrder(Order order){
        try {
            orderController.updateOrder(order);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").build();
        } catch (Exception e){
            return Response.status(Response.Status.CONFLICT).entity("Order could not be updated").build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("Admin")
    public Response updateOrderItem(@PathParam("id") int id, OrderItem orderItem){
        try {
            orderController.updateOrderItem(id, orderItem);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Order item was not found").build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).entity("Order item could not be updated").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    public Response deleteOrder(@PathParam("id") int id){
        try {
            orderController.deleteOrder(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).entity("Order could not be deleted").build();
        }
    }

    @DELETE
    @Path("/{id}/{productId}")
    @RolesAllowed("Admin")
    public Response deleteOrderItem(@PathParam("id") int id, @PathParam("productId") int productId){
        try {
            orderController.deleteOrderItem(id, productId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Order item was not found").build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).entity("Order item could not be deleted").build();
        }
    }

    @GET
    @Path("/all/{pagesize}/{page}")
    @RolesAllowed("Admin")
    public Response getOrderList(@PathParam("pagesize") int pageSize, @PathParam("page") int page) {
        try {
            return Response.status(Response.Status.OK).entity(orderController.getOrderList(pageSize, page)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("Admin")
    public Response getOrder(@PathParam("id") int orderId){
        try {
            return Response.ok().entity(orderController.getOrder(orderId)).build();
        } catch (SQLException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Order with id " + orderId + " was not found").build();
        }
    }
}
