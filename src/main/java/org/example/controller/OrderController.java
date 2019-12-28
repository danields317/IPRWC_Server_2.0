package org.example.controller;

import org.example.db.OrderDAO;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderController {

    private final OrderDAO orderDAO;

    public OrderController(Jdbi jdbi){
        orderDAO = jdbi.onDemand(OrderDAO.class);
    }

    public Order getOrder(int id) throws SQLException {
        try {
            List<Order> tempOrder = orderDAO.getOrder(id);
            return tempOrder.get(tempOrder.size() -1);
        } catch (Exception e){
            throw new SQLException();
        }

    }


    public void placeOrder(Order order) throws Exception {
        try {
            int orderId = orderDAO.addOrder(order.getAccountId(), order.getDeliveryCity(), order.getDeliveryAddress(),
                    order.getDeliveryNumber(), order.getDeliveryDate());
            for (OrderItem item: order.getItems()){
                orderDAO.addOrderItem(orderId, item.getProductId(), item.getAmount());
            }
        } catch (Exception e){
            throw e;
        }

    }

    public void updateOrder(Order order) {
        try {
            boolean succes = orderDAO.updateOrder(order.getId(), order.getDeliveryCity(), order.getDeliveryAddress(),
                    order.getDeliveryNumber(), order.getDeliveryDate());
            if (!succes){
                throw new NoSuchElementException();
            }
        } catch (Exception e){
            throw e;
        }
    }

    public void updateOrderItem(int id, OrderItem orderItem) {
        try {
            boolean succes = orderDAO.updateOrderItem(id, orderItem.getProductId(), orderItem.getAmount());
            if (!succes){
                throw new NoSuchElementException();
            }
        } catch (Exception e){
            throw e;
        }
    }

    public void deleteOrder(int id) {
        try {
            boolean succes = orderDAO.deleteOrder(id);
            if (!succes){
                throw new NoSuchElementException();
            }
        } catch (Exception e){
            throw e;
        }
    }

    public void deleteOrderItem(int orderId, int productId) {
        try {
            boolean succes = orderDAO.deleteOrderItem(orderId, productId);
            if (!succes){
                throw new NoSuchElementException();
            }
        } catch (Exception e){
            throw e;
        }
    }

}
