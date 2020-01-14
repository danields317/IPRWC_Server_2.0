package org.example.model;

import java.util.List;

public class OrderList {

    private List<Order> orders;
    private int totalPages;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(double totalPages) {
        this.totalPages = (int) Math.ceil(totalPages);
    }
}
