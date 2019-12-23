package org.example.mapper;

import org.example.model.ProductImage;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductImageMapper implements RowMapper<ProductImage> {

    @Override
    public ProductImage map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new ProductImage(rs.getInt("id"), rs.getString("image_location"));
    }
}
