package org.example.db;

import org.example.mapper.ProductImageMapper;
import org.example.mapper.ProductMapper;
import org.example.model.Product;
import org.example.model.ProductImage;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ProductDAO {

    @SqlQuery("SELECT * FROM product LIMIT :pagesize OFFSET :offset")
    @RegisterRowMapper(ProductMapper.class)
    List<Product> getProductList(@Bind("pagesize") int pageSize, @Bind("offset") int offset);

    @SqlQuery("SELECT (COUNT(*)::DECIMAL / :pagesize) FROM product")
    double getMaxPages(@Bind("pagesize") int pageSize);

    @SqlQuery("SELECT * FROM product WHERE id = :id")
    @RegisterRowMapper(ProductMapper.class)
    Product readProductById(@Bind("id") int id);

    @SqlQuery("SELECT id, image_location FROM product_image WHERE product_id = :product_id")
    @RegisterRowMapper(ProductImageMapper.class)
    List<ProductImage> readProductImagesById(@Bind("product_id") int productId);

    @SqlUpdate("DELETE FROM product WHERE id = :id")
    boolean deleteProductById(@Bind("id") int id);

    @SqlUpdate("DELETE FROM product_image WHERE product_id = :productId AND id = :imageId")
    boolean deleteProductImageById(@Bind("productId") int productId, @Bind("imageId") int imageId);

    @SqlUpdate("INSERT INTO product (product_name, description, brand, price, stock) VALUES (?, ? , ?, ?, ?)")
    @GetGeneratedKeys("id")
    int createProduct(String productName, String description, String brand, double price, int stock);

    @SqlUpdate("UPDATE product SET thumbnail = :imageLocation WHERE id = :id")
    void setThumbnail(@Bind("imageLocation") String imageLocation, @Bind("id") int id);

    @SqlUpdate("UPDATE product SET product_name = :productName, description = :description, brand = :brand, price = :price, stock = :stock WHERE id =:id")
    void updateProduct(@Bind("id") int id, @Bind("productName") String productName, @Bind("description") String description,
                       @Bind("brand") String brand, @Bind("price") double price, @Bind("stock") int stock);
}
