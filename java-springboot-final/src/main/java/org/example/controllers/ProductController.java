package org.example.controllers;

import org.example.daos.ProductDao;
import org.example.daos.UserDao;
import org.example.models.Order;
import org.example.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @GetMapping
    public List<Product> getProducts(){
        return productDao.getAllProduct();
    }

    @GetMapping(path = "/{id}")
    public Product getProductById(@PathVariable int id){
        return productDao.getProductById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("permitAll()")
    public Product createProduct(@RequestBody Product product){
        return productDao.createProduct(product);
    }

    @PutMapping(path = "/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable int id){
        return productDao.updateProduct(product, id);
    }

    @DeleteMapping(path = "/{id}")
    public int deleteProduct(@PathVariable int id){
        return productDao.deleteProduct(id);
    }


}
