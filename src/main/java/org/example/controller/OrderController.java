package org.example.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.core.JWTManager;
import org.example.db.OrderDAO;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.OrderList;
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

    public Order getPersonalOrder(int id, String token) throws SQLException {
        int personalId = getId(token);
        try {
            List<Order> tempOrder = orderDAO.getOrder(id);
            Order order = tempOrder.get(tempOrder.size() -1);
            if (order.getAccountId() == personalId) {
                return order;
            } else {
                throw new Exception();
            }
        } catch (Exception e){
            throw new SQLException();
        }
    }

    public OrderList getPersonalOrderList(int pageSize, int page, String token) throws SQLException {
        int personalId = getId(token);
        int offset = ((page -1) * pageSize);
        OrderList orderList = new OrderList();
        try {
            orderList.setOrders(orderDAO.getPersonalOrderList(pageSize, offset, personalId));
            orderList.setTotalPages(orderDAO.getPersonalMaxPages(pageSize, personalId));
            return orderList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public OrderList getOrderList(int pageSize, int page) throws SQLException {
        int offset = ((page -1) * pageSize);
        OrderList orderList = new OrderList();
        try {
            orderList.setOrders(orderDAO.getOrderList(pageSize, offset));
            orderList.setTotalPages(orderDAO.getMaxPages(pageSize));
            return orderList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }


    public void placeOrder(String token, Order order) throws Exception {
        int personalId = getId(token);
        if (personalId == order.getAccountId()) {
            try {
                int orderId = orderDAO.addOrder(order.getAccountId(), order.getDeliveryCity(), order.getDeliveryAddress(),
                        order.getDeliveryNumber(), order.getDeliveryDate());
                for (OrderItem item: order.getItems()){
                    orderDAO.addOrderItem(orderId, item.getProductId(), item.getAmount());
                }
            } catch (Exception e){
                throw e;
            }
        } else throw new IllegalAccessException();
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

    private int getId(String token) throws JWTDecodeException {
        token = stripToken(token);
        JWTManager jwtManager = JWTManager.getJwtManager();
        DecodedJWT jwt = jwtManager.decodeJwt(token);
        return jwt.getClaim("accountId").asInt();
    }

    private String stripToken(String token){
        return token.split("Bearer ")[1];
    }

}
