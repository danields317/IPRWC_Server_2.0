package org.example.model;
import java.util.List;

public class Product {

    private int id;
    private String productName;
    private String description;
    private String brand;
    private double price;
    private int stock;
    private String thumbnail;
    private List<ProductImage> images;
    private String category;

    public Product(){

    }

    public Product(String productName, String description, String brand, double price, int stock, String category) {
        this.productName = productName;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public Product(int id, String productName, String description, String brand, double price, String category) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.category = category;
    }

    public Product(int id, String productName, String description, String brand, double price, int stock, String category) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public Product(int id, String productName, String description, String brand, double price, int stock, String thumbnail, String category) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.thumbnail = thumbnail;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
