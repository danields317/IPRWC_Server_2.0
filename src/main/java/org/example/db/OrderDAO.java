package org.example.db;

import org.example.mapper.FullOrderMapper;
import org.example.mapper.OrderMapper;
import org.example.model.Order;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.joda.time.DateTime;

import java.util.List;

public interface OrderDAO {

    @SqlQuery("SELECT (COUNT(*)::DECIMAL / :pagesize) FROM customer_order")
    double getMaxPages(@Bind("pagesize") int pageSize);

    @SqlQuery("SELECT (COUNT(*)::DECIMAL / :pagesize) FROM customer_order WHERE account_id = :accountId")
    double getPersonalMaxPages(@Bind("pagesize") int pageSize, @Bind("accountId") int accountId);

    @SqlQuery("SELECT * FROM customer_order ORDER BY delivery_date LIMIT :pagesize OFFSET :offset")
    @RegisterRowMapper(OrderMapper.class)
    List<Order> getOrderList(@Bind("pagesize") int pageSize, @Bind("offset") int offset);

    @SqlQuery("SELECT * FROM customer_order WHERE account_id = :accountId ORDER BY delivery_date LIMIT :pagesize OFFSET :offset")
    @RegisterRowMapper(OrderMapper.class)
    List<Order> getPersonalOrderList(@Bind("pagesize") int pageSize, @Bind("offset") int offset, @Bind("accountId") int accountId);

    @SqlQuery("SELECT co.id, co.account_id, co.delivery_city, co.delivery_address, co.delivery_number, co.delivery_date, coi.product_id, coi.amount, " +
            "p.product_name, p.description, p.brand, p.price,\n" +
            "p.category FROM customer_order co JOIN customer_order_item coi ON co.id = coi.order_id\n" +
            "JOIN product p ON p.id = coi.product_id\n" +
            "WHERE co.id = :orderId")
    @RegisterRowMapper(FullOrderMapper.class)
    List<Order> getOrder(@Bind("orderId") int orderId);

    @SqlUpdate("INSERT INTO customer_order (account_id, delivery_city, delivery_address, delivery_number, delivery_date) VALUES " +
            "(?, ?, ?, ? ,?)")
    @GetGeneratedKeys("id")
    int addOrder(int accountId, String deliveryCity, String deliveryAddress, String deliveryNumber, DateTime deliveryDate);

    @SqlUpdate("INSERT INTO customer_order_item VALUES (?, ?, ?)")
    int addOrderItem(int orderId, int productId, int amount);

    @SqlUpdate("UPDATE customer_order SET delivery_city = :deliveryCity, delivery_address = :deliveryAddress, " +
            "delivery_number = :deliveryNumber, delivery_date = :deliveryDate WHERE id = :id")
    boolean updateOrder(@Bind("id") int id, @Bind("deliveryCity") String deliveryCity, @Bind("deliveryAddress") String deliveryAddress,
                        @Bind("deliveryNumber") String deliveryNumber, @Bind("deliveryDate") DateTime deliveryDate);

    @SqlUpdate("UPDATE customer_order_item SET amount = :amount WHERE order_id = :orderId AND product_id = :productId")
    boolean updateOrderItem(@Bind("orderId") int orderId, @Bind("productId") int productId, @Bind("amount") int amount);

    @SqlUpdate("DELETE FROM customer_order WHERE id = :orderId")
    boolean deleteOrder(@Bind("orderId") int orderId);

    @SqlUpdate("DELETE FROM customer_order_item WHERE order_id = :orderId AND product_id = :productId")
    boolean deleteOrderItem(@Bind("orderId") int orderId, @Bind("productId") int productId);




}
