package org.example.db;

import org.example.mapper.FullOrderMapper;
import org.example.mapper.OrderMapper;
import org.example.model.Order;
import org.example.model.Product;
import org.jdbi.v3.sqlobject.config.RegisterJoinRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface OrderDAO {

    @SqlQuery("SELECT * FROM customer_order LIMIT :pagesize OFFSET :offset")
    @RegisterJoinRowMapper(OrderMapper.class)
    List<Order> getProductList(@Bind("pagesize") int pageSize, @Bind("offset") int offset);

    @SqlQuery("SELECT co.id, co.account_id, co.delivery_city, co.delivery_address, co.delivery_number, co.delivery_date, coi.product_id, coi.amount, p.product_name, p.description, p.brand, p.price\n" +
            "FROM customer_order co JOIN customer_order_item coi ON co.id = coi.order_id\n" +
            "JOIN product p ON p.id = coi.product_id\n" +
            "WHERE co.id = :orderId")
    @RegisterRowMapper(FullOrderMapper.class)
    List<Order> getOrder(@Bind("orderId") int orderId);


}
