package org.example.daos;

import org.example.exceptions.DaoException;
import org.example.models.Order;
import org.example.models.User;
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
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

   public List<Order> getAll(){
        return jdbcTemplate.query("SELECT * FROM orders ORDER BY id;", this::mapToOrder);
    }

    public Order getById(int id){
        try{
            return jdbcTemplate.queryForObject("SELECT * from orders WHERE id =?;", this::mapToOrder,id);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }

    public Order createOrder(Order order){
        int sqlId = getAll().size() +1;

        String sql = "INSERT INTO orders (id,username) VALUES(?,?);";
        try{
            jdbcTemplate.update(sql, sqlId,  order.getUsername());
            return getById(sqlId);
        }
        catch(EmptyResultDataAccessException e){
            throw new DaoException("Failed to create order");
        }
    }

    public Order updateOrder(Order order){
        String sql = "UPDATE orders SET username = ? WHERE id = ?;";

        int rowsAffected = jdbcTemplate.update(sql, order.getUsername(), order.getId());

        if (rowsAffected == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        else{
            return getById(order.getId());
        }
    }

    public int deleteOrder(int id){
        String sql = "DELETE FROM orders WHERE id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0 ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        else{
            return rowsAffected;
        }
    }




    private Order mapToOrder(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        return new Order(
                id,
                resultSet.getString("username")
        );
    }


}
