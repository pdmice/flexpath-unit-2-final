package org.example.controllers;


import org.example.daos.OrderItemDao;
import org.example.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/order-items")
@PreAuthorize("hasAuthority('ADMIN')")
public class OrderItemController {

    @Autowired
    private OrderItemDao orderItemDao;

    @GetMapping
    public List<OrderItem> getAllOI() {
        return orderItemDao.getAllOI();
    }

    @GetMapping(path = "/{id}")
    public OrderItem getById(@PathVariable int id){
        return orderItemDao.getOIById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("permitAll()")
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem){
        return orderItemDao.createOI(orderItem);
    }

    @PutMapping("/id")
    public OrderItem updateOrderItem(@RequestBody OrderItem orderItem, @PathVariable int id){
        return orderItemDao.updateOI(orderItem, id);
    }

    @DeleteMapping("/id")
    public int deleteOrderItem(@PathVariable int id){
        return orderItemDao.deleteOI(id);
    }






}
