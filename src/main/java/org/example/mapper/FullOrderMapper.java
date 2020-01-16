package org.example.mapper;

import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FullOrderMapper implements RowMapper<Order> {

    private boolean first = true;
    private Order order;

    @Override
    public Order map(ResultSet rs, StatementContext ctx) throws SQLException {
        if (first){
            first = false;
            createOrder(rs);
        } else {
            try {
                addOrderItem(rs);
            } catch (Exception e){
                return this.order;
            }
        }
        return this.order;
    }

    private void createOrder(ResultSet rs) throws SQLException {
        order = new Order(rs.getInt("id"), rs.getInt("account_id"), rs.getString("delivery_city"),
                rs.getString("delivery_address"), rs.getString("delivery_number"), rs.getDate("delivery_date"));
        addOrderItem(rs);
    }

    private void addOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem(new Product(
                rs.getInt("product_id"), rs.getString("product_name"), rs.getString("description"),
                rs.getString("brand"), rs.getDouble("price"), rs.getString("category")
        ), rs.getInt("amount"));
        this.order.setItem(orderItem);
    }

}
