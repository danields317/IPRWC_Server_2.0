package org.example.model;

public class OrderItem {

    private Product product;
    private int productId;
    private int amount;

    public OrderItem(){}

    public OrderItem(Product product, int amount) {
        this.product = product;
        this.amount = amount;
        this.productId = product.getId();
    }

    public OrderItem(int productId, int amount){
        this.productId = productId;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
