package org.example.controller;

import org.example.db.ProductDAO;
import org.example.model.Product;
import org.example.model.ProductImage;
import org.example.model.ProductList;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductController {

    private final ProductDAO productDAO;

    public ProductController(Jdbi jdbi){
        this.productDAO = jdbi.onDemand(ProductDAO.class);
    }

    public Product getProductWithId(int id) throws SQLException {
        Product product = productDAO.readProductById(id);
        if (product != null){
            product.setImages(productDAO.readProductImagesById(id));
            return product;
        }else {
            throw new SQLException();
        }
    }

    public List<ProductImage> getProductImagesWithId(int id) throws NullPointerException {
        List<ProductImage> images = productDAO.readProductImagesById(id);
        if (images != null && !images.isEmpty()){
            return images;
        } else {
            throw new NullPointerException();
        }
    }

    public ProductList getProducts(int pageSize, int page) throws SQLException {
        int offset = ((page -1) * pageSize);
        ProductList productList = new ProductList();
        try {
            productList.setProducts(productDAO.getProductList(pageSize, offset));
        } catch (Exception e){
            throw new SQLException();
        }
        try {
            productList.setTotalPages(productDAO.getMaxPages(pageSize));
        } catch (Exception e){
            throw new SQLException();
        }
        return productList;
    }

    public void deleteProductWithId(int id) throws NoSuchElementException{
        try {
            boolean succes = productDAO.deleteProductById(id);
            if (!succes){
                throw new NoSuchElementException();
            }
        } catch (Exception e){
            throw e;
        }
    }

    public void deleteProductImageWithId(int productId, int imageId) throws NoSuchElementException{
        try {
            boolean succes = productDAO.deleteProductImageById(productId, imageId);
            if (!succes){
                throw new NoSuchElementException();
            }
        } catch (Exception e){
            throw e;
        }
    }
}
