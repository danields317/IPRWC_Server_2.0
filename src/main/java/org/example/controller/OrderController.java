package org.example.controller;

import org.example.db.OrderDAO;
import org.example.model.Order;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class OrderController {

    private final OrderDAO orderDAO;

    public OrderController(Jdbi jdbi){
        orderDAO = jdbi.onDemand(OrderDAO.class);
    }

    public Order test(){
        List<Order> tempOrder = orderDAO.getOrder(1);
        return tempOrder.get(tempOrder.size() -1);
    }



}
