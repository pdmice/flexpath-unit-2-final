package org.example.daos;

import org.example.models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Product> getAllProduct(){
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, this::mapToProduct);
    }

    public Product getProductById(int id){
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapToProduct, id);
    }

    public Product createProduct(Product product){
        String sql = "INSERT INTO products (id, name, price) VALUES(?, ? ,?)";
        jdbcTemplate.update(sql, product.getId(),product.getName(), product.getPrice());
        return getProductById(product.getId());
    }

    public Product updateProduct(Product product, int id){
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(),product.getPrice(), product.getId());
        return getProductById(product.getId());
    }

    public int deleteProduct(int id){
        String sql = "DELETE FROM products where id = ?";
        return jdbcTemplate.update(sql, id);
    }


    private Product mapToProduct(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        return new Product(
                id,
                resultSet.getString("name"),
                resultSet.getBigDecimal("price")
        );
    }

}
