package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
//@PreAuthorize("hasAuthority('ADMIN')")


public class OrderController {

    @Autowired
    private OrderDao orderDao;

    @GetMapping
    public List<Order> getAll(){
        return orderDao.getAll();
    }

    @GetMapping(path = "/{id}")
    public Order getById(@PathVariable  int id){
        return orderDao.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("permitAll()")
    public Order createOrder(@RequestBody Order order){
        return orderDao.createOrder(order);
    }

    @PutMapping("/id")
    public Order updateOrder(@RequestBody Order order){
        return orderDao.updateOrder(order);
    }

    @DeleteMapping("/id")
    public int deleteOrder(@PathVariable int id){
        return orderDao.deleteOrder(id);
    }




}
