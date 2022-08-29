package com.example.springcloudspanner.controller;

import com.example.springcloudspanner.model.Order;
import com.example.springcloudspanner.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody Order order){
        String message = this.orderService.save(order);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrders(@PathVariable int id){
        Order order = this.orderService.findByOrderId(id);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @GetMapping("/orders/order/{name}")
    public ResponseEntity<List<Order>> findOrderByName(@PathVariable String name){
        List<Order> orders = this.orderService.findOrdersByName(name);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
