package org.example.daos;

import org.example.exceptions.DaoException;
import org.example.models.OrderItem;
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
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<OrderItem> getAllOI(){
        String sql = "SELECT * FROM order_items;";
        return jdbcTemplate.query(sql, this::mapToOrderItem);
    }


    public OrderItem getOIById(int id){
        String sql = "SELECT * FROM order_items WHERE id = ?;";
        try{
            return jdbcTemplate.queryForObject(sql,this::mapToOrderItem ,id);
        }
        catch(EmptyResultDataAccessException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }

    public OrderItem createOI(OrderItem orderItem){
        int SqlId = getAllOI().size() +1 ;
        String sql = "INSERT INTO order_items (id, order_id, product_id, quantity) VALUES(?,?,?,?);";

        try{
        jdbcTemplate.update(sql, SqlId, orderItem.getOrderId(),orderItem.getProductId(),orderItem.getQuantity());
        return getOIById(SqlId);}
        catch(EmptyResultDataAccessException e ){
            throw new DaoException("Failed to create product");
        }
    }

    public OrderItem updateOI(OrderItem orderItem, int id){
        String sql = "UPDATE order_items SET order_id = ?, product_id = ?, quantity = ? WHERE id = ?;";
        jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity(), orderItem.getId());
        return getOIById(orderItem.getId());
    }

    public int deleteOI(int id){
        String sql = "DELETE FROM order_items where id = ?;";
        int rowAffected = jdbcTemplate.update(sql, id);
        if (rowAffected == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        else{
            return rowAffected;
        }
    }



    private OrderItem mapToOrderItem(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        return new OrderItem(
                id,
                resultSet.getInt("order_id"),
                resultSet.getInt("product_id"),
                resultSet.getInt("quantity")

        );
    }
}
