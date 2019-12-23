package org.example.model;

import java.util.List;

public class ProductList {

    private List<Product> products;
    private int totalPages;

    public ProductList(){

    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(double totalPages) {
        this.totalPages = (int) Math.ceil(totalPages);
    }

}
