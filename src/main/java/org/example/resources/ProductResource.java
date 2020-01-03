package org.example.resources;


import org.example.controller.ProductController;
import org.example.model.Product;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jdbi.v3.core.Jdbi;
import javax.activation.UnsupportedDataTypeException;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

@Path("/api/product")
public class ProductResource {

    private final ProductController productController;

    public ProductResource(Jdbi jdbi){
        this.productController = new ProductController(jdbi);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadProduct(@FormDataParam("thumbnail")InputStream image,
                                       @FormDataParam("thumbnail")FormDataContentDisposition imageDetail,
                                       @FormDataParam("productName") String productName,
                                       @FormDataParam("description") String description,
                                       @FormDataParam("brand") String brand,
                                       @FormDataParam("price") double price,
                                       @FormDataParam("stock") int stock,
                                       @FormDataParam("category") String category
                                       ){
        try {
            Product product = new Product(productName, description, brand, price, stock, category);
            productController.uploadProduct(image, product, imageDetail.getFileName());
            return Response.ok().build();
        } catch (UnsupportedDataTypeException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Image is not of type jpg, jpeg or png").build();
        } catch (IOException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while storing image").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to create product due to unforeseen circumstances").build();
        }
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateProduct(@FormDataParam("thumbnail")InputStream image,
                                       @FormDataParam("thumbnail")FormDataContentDisposition imageDetail,
                                       @FormDataParam("id") int id,
                                       @FormDataParam("productName") String productName,
                                       @FormDataParam("description") String description,
                                       @FormDataParam("brand") String brand,
                                       @FormDataParam("price") double price,
                                       @FormDataParam("stock")int stock,
                                       @FormDataParam("category") String category
    ){
        try {
            Product product = new Product(id, productName, description, brand, price, stock, category);
            productController.updateProduct(image, product, imageDetail.getFileName());
            return Response.ok().build();
        } catch (UnsupportedDataTypeException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Image is not of type jpg, jpeg or png").build();
        } catch (IOException e) {
            return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).entity("Error while storing image").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to update product due to unforeseen circumstances").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("id") int id){
        try{
            return Response.ok(productController.getProductWithId(id)).build();
        } catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).entity("Product with id " + id + " was not found").build();
        }
    }

    @GET
    @Path("/{id}/thumbnail")
    @Produces("image/jpg")
    public Response getProductThumbnail(@PathParam("id") int id){
        try{
            return Response.ok(productController.getThumbnail(id)).build();
        } catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).entity("Product with id " + id + " was not found").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("id") int id){
        try{
            productController.deleteProductWithId(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Product with id " + id + " was not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).entity("Product with id " + id + " could not be deleted").build();
        }
    }

    @POST
    @Path("/img/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProductImage(@PathParam("id") int productId){
        try {
            productController.uploadProductImage(productId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/img/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductImages(@PathParam("id") int id){
        try {
            return Response.status(Response.Status.OK).entity(productController.getProductImagesWithId(id)).build();
        } catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).entity("Images linked with product id " + id + " were not found").build();
        }
    }

    @DELETE
    @Path("/img/{productId}/{imageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProductImage(@PathParam("productId") int productId, @PathParam("imageId") int imageId){
        try {
            productController.deleteProductImageWithId(productId, imageId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Product image with id " + imageId + " from product with id " + productId + " was not found").build();
        } catch (Exception e){
            return Response.status(Response.Status.CONFLICT).entity("Product image with id " + imageId + " from product with id " + productId + " could not be deleted").build();
        }
    }

    @GET
    @Path("/{type}/{pagesize}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(@PathParam("type") String type, @PathParam("pagesize") int pageSize, @PathParam("page") int page){
        try {
            return Response.status(Response.Status.OK).entity(productController.getProducts(type, pageSize, page)).build();
        } catch (Exception e){
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }
}
