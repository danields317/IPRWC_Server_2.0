package org.example.resources;

import com.auth0.jwt.exceptions.JWTDecodeException;
import org.example.controller.AccountController;
import org.example.model.Account;
import org.jdbi.v3.core.Jdbi;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.NoSuchElementException;


@Path("/api/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private final AccountController accountController;

    public AccountResource(Jdbi jdbi){
        this.accountController = new AccountController(jdbi);
    }

    @GET
    @PermitAll
    public Response getPersonalAccount(@HeaderParam("Authorization") String token){
        try {
            return Response.status(Response.Status.OK).entity(accountController.getPersonalAccount(token)).build();
        }catch (JWTDecodeException e){
            return Response.status(Response.Status.CONFLICT).entity("Token could not be validated").build();
        }
    }

    @PUT
    @PermitAll
    public Response updatePersonalAccount(@HeaderParam("Authorization") String token, Account account){
        try{
            accountController.updatePersonalAccount(token, account);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (JWTDecodeException e){
            return Response.status(Response.Status.CONFLICT).entity("Token could not be validated").build();
        } catch (SQLException e){
            return Response.status(Response.Status.CONFLICT).entity("Unable to update account").build();
        }
    }

    @DELETE
    @PermitAll
    public Response deletePersonalAccount(@HeaderParam("Authorization") String token){
        try{
            accountController.deletePersonalAccount(token);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (JWTDecodeException e){
            return Response.status(Response.Status.CONFLICT).entity("Token could not be validated").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.CONFLICT).entity("Unable to delete account").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomerAccount(Account account){
        try {
            accountController.createCustomerAccount(account);
            return Response.status(Response.Status.CREATED).build();
        } catch (NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or invalid parameters").build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).entity("Unable to create account").build();
        }
    }

    @POST
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    public Response addAdminAccount(Account account){
        try {
            accountController.createAccount(account);
            return Response.status(Response.Status.CREATED).build();
        } catch (NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or invalid parameters").build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).entity("Unable to create account").build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("Admin")
    public Response getAccount(@PathParam("id") int accountId){
        try {
            return Response.status(Response.Status.OK).entity(accountController.getAccountWithId(accountId)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Account with id " + accountId + " was not found").build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("Admin")
    public Response updateAccount(@PathParam("id") int accountId, Account account) {
        try {
            accountController.updateAccountWithId(accountId, account);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or invalid parameters").build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Account with id " + accountId + " was not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).entity("Account with id " + accountId + " could not be updated").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    public Response deleteAccount(@PathParam("id") int accountId) {
        try {
            accountController.deleteAccountWithId(accountId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Account with id " + accountId + " was not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).entity("Account with id " + accountId + " could not be deleted").build();
        }
    }

    @GET
    @Path("/all/{pagesize}/{page}")
    @RolesAllowed("Admin")
    public Response getAccounts(@PathParam("pagesize") int pageSize, @PathParam("page") int page){
        try {
            return Response.status(Response.Status.OK).entity(accountController.getAccounts(pageSize, page)).build();
        } catch (Exception e){
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }
}
