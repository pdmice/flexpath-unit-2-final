package org.example.daos;

import org.example.exceptions.DaoException;
import org.example.models.Product;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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
        String sql = "SELECT * FROM products;";
        return jdbcTemplate.query(sql, this::mapToProduct);
    }

    public Product getProductById(int id){

        String sql = "SELECT * FROM products WHERE id = ?;";
        try{
        return jdbcTemplate.queryForObject(sql, this::mapToProduct, id);}
        catch(EmptyResultDataAccessException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }

    public Product createProduct(Product product){
        int sqlId = getAllProduct().size() + 1;
        String sql = "INSERT INTO products (id, name, price) VALUES(?, ? ,?);";
        try {
            jdbcTemplate.update(sql, sqlId, product.getName(), product.getPrice());
            return getProductById(sqlId);
        }
        catch(EmptyResultDataAccessException e){
            throw new DaoException("Failed to create order");
        }

    }

    public Product updateProduct(Product product, int id){
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, product.getName(),product.getPrice(), product.getId());
        if(rowsAffected == 0 ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        else{
        return getProductById(product.getId());}
    }

    public int deleteProduct(int id){
        String sql = "DELETE FROM products where id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        else{
            return rowsAffected;
        }
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
