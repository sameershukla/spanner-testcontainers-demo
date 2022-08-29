package com.example.springcloudspanner.service;

import com.example.springcloudspanner.model.Order;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface OrderService {

    public String save(Order order);

    public Order findByOrderId(Integer id);

    public List<Order> findOrdersByName(String name);

}
