package org.example.daos;

import org.example.models.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
        return jdbcTemplate.queryForObject(sql,this::mapToOrderItem ,id);
    }

    public OrderItem createOI(OrderItem orderItem){
        String sql = "INSERT INTO order_items (id, order_id, product_id, quantity) VALUES(?,?,?,?);";
        jdbcTemplate.update(sql, orderItem.getId(), orderItem.getOrderId(),orderItem.getProductId(),orderItem.getQuantity());
        return getOIById(orderItem.getId());
    }

    public OrderItem updateOI(OrderItem orderItem, int id){
        String sql = "UPDATE order_items SET order_id = ?, product_id = ? quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity(), orderItem.getId());
        return getOIById(orderItem.getId());
    }

    public int deleteOI(int id){
        String sql = "DELETE FROM order_items where id = ?;";
        return jdbcTemplate.update(sql, id);
    }



    private OrderItem mapToOrderItem(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        return new OrderItem(
                id,
                resultSet.getInt("order_item"),
                resultSet.getInt("product_id"),
                resultSet.getInt("quantity")

        );
    }
}
