package org.example.mapper;

import org.example.model.Order;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;


public class OrderMapper implements RowMapper<Order> {

    @Override
    public Order map(ResultSet rs, StatementContext ctx) throws SQLException {
        return null;
    }
}
