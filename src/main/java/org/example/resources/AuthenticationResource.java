package org.example.resources;

import org.example.controller.AuthenticationController;
import org.example.model.Account;
import org.example.model.Authentication;
import org.jdbi.v3.core.Jdbi;

import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    private final AuthenticationController authenticationController;

    public AuthenticationResource(Jdbi jdbi){
        this.authenticationController = new AuthenticationController(jdbi);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(Authentication authentication){
        try {
            return Response.status(Response.Status.OK).entity(authenticationController.authenticate(authentication)).build();
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }

}
