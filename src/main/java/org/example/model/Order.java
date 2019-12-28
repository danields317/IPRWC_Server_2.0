package org.example.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {

    private int id;
    private int accountId;
    private String deliveryCity;
    private String deliveryAddress;
    private String deliveryNumber;
    private DateTime deliveryDate;
    List<OrderItem> items;

    public Order(){}

    public Order(int id, int accountId, String deliveryCity, String deliveryAddress, String deliveryNumber, Date deliveryDate) {
        this.id = id;
        this.accountId = accountId;
        this.deliveryCity = deliveryCity;
        this.deliveryAddress = deliveryAddress;
        this.deliveryNumber = deliveryNumber;
        this.deliveryDate = new DateTime(deliveryDate);
        this.items = new ArrayList<>();
    }

    public void setItem(OrderItem item){
        this.items.add(item);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public DateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(DateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
