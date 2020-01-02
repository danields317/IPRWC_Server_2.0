package org.example.resources;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.controller.AuthenticationController;
import org.example.core.JWTManager;
import org.example.model.Account;
import org.example.model.Authentication;
import org.example.model.JsonToken;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.PermitAll;
import javax.naming.AuthenticationException;
import javax.ws.rs.*;
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

    @GET
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getRefreshtoken(@HeaderParam("Authorization") String token){
        try {
            return Response.status(Response.Status.OK).entity(authenticationController.refreshAuthentication(token)).build();
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("JWT token is not eligible for a refresh").build();
        }
    }
}
