package org.example.controller;

import org.apache.commons.io.FileUtils;
import org.example.db.ProductDAO;
import org.example.model.Product;
import org.example.model.ProductImage;
import org.example.model.ProductList;
import org.jdbi.v3.core.Jdbi;

import javax.activation.UnsupportedDataTypeException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public ProductList getProducts(String type, int pageSize, int page) throws SQLException {
        if (type.equals("all")) {
            return getAllProducts(pageSize, page);
        } else {
            return getProductsByType(type, pageSize, page);
        }

    }

    private ProductList getAllProducts(int pageSize, int page) throws SQLException {
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

    private ProductList getProductsByType(String type, int pageSize, int page) throws SQLException {
        int offset = ((page -1) * pageSize);
        ProductList productList = new ProductList();
        try {
            productList.setProducts(productDAO.getProductListWithCategory(type, pageSize, offset));
        } catch (Exception e){
            throw new SQLException();
        }
        try {
            productList.setTotalPages(productDAO.getMaxPagesWithCategory(type, pageSize));
        } catch (Exception e){
            throw new SQLException();
        }
        return productList;
    }

    public void deleteProductWithId(int id) throws NoSuchElementException, IOException {
        try {
            boolean succes = productDAO.deleteProductById(id);
            if (!succes){
                throw new NoSuchElementException();
            }
        } catch (Exception e){
            throw e;
        }
        removeProductFolder(generateProductFolderPath(id));
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

    public void uploadProduct(InputStream thumbnail, Product product, String fileName) throws IOException {
        if (!checkFileExtension(fileName)){
            throw new UnsupportedDataTypeException();
        }
        int id = productDAO.createProduct(product.getProductName(), product.getDescription(), product.getBrand(), product.getPrice(), product.getStock());
        String imageLocation = storeImage(thumbnail, id, "thumbnail.jpg");
        productDAO.setThumbnail(imageLocation, id);
    }

    public void updateProduct(InputStream thumbnail, Product product, String fileName) throws IOException {
        if (!checkFileExtension(fileName)){
            throw new UnsupportedDataTypeException();
        }
        productDAO.updateProduct(product.getId(), product.getProductName(), product.getDescription(), product.getBrand(), product.getPrice(), product.getStock());
        String imageLocation = storeImage(thumbnail, product.getId(), "thumbnail.jpg");
        productDAO.setThumbnail(imageLocation, product.getId());
    }

    public String storeImage(InputStream image, int product_id, String fileName) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"));
        path = path.getParent();
        StringBuilder pathBuilder = new StringBuilder();
        String location = pathBuilder.append(path.toString()).append("/").append("Products/").append(product_id).append("/").append(fileName).toString();
        String styledLocation = styleLocation(location);
        File targetFile = new File(styledLocation);

        copyInputStreamToFile(image, targetFile);

        return styledLocation;
    }

    public void uploadProductImage(int productId) {
        //TO DO
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        if (!file.exists()){
            file.getParentFile().mkdirs();
        }
        FileUtils.copyInputStreamToFile(inputStream, file);
    }

    private void removeProductFolder(File filepath) throws IOException {
        FileUtils.deleteDirectory(filepath);
    }

    private String styleLocation(String location){
        return location.replace("\\", "/");
    }

    private boolean checkFileExtension(String fileName){
        String[] file = (fileName.split("\\."));
        String suffix = file[file.length -1].toLowerCase();
        if (suffix.equals("jpg") || suffix.equals("jpeg") || suffix.equals("png")){
            return true;
        }else {
            return false;
        }
    }

    private File generateProductFolderPath(int id){
        Path path = Paths.get(System.getProperty("user.dir"));
        path = path.getParent();
        StringBuilder pathBuilder = new StringBuilder();
        String location = pathBuilder.append(path.toString()).append("/").append("Products/").append(id).toString();
        return new File(location);
    }


    public File getThumbnail(int id) throws FileNotFoundException {
        try {
            Path path = Paths.get(System.getProperty("user.dir"));
            path = path.getParent();
            StringBuilder pathBuilder = new StringBuilder();
            String location = pathBuilder.append(path.toString()).append("/").append("Products/").append(id).append("/").append("thumbnail.jpg").toString();
            File file = new File(location);
            if(!file.exists()){
                throw new FileNotFoundException("File not found");
            }
            return file;
        } catch (Exception e){
            throw e;
        }

    }
}
