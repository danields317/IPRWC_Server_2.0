package org.example.mapper;

import org.example.model.Product;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {

    @Override
    public Product map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Product(rs.getInt("id"), rs.getString("product_name"), rs.getString("description"),
                rs.getString("brand"), rs.getDouble("price"), rs.getInt("stock"), rs.getString("thumbnail"));
    }
}
