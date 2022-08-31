package com.example.springcloudspanner.service;

import com.example.springcloudspanner.db.entity.OrderEntity;
import com.example.springcloudspanner.db.repository.OrderRepository;
import com.example.springcloudspanner.model.Converter;
import com.example.springcloudspanner.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public String save(Order order) {
        try{
            OrderEntity orderEntity = Converter.convertDTOToEntity(order);
            orderEntity.setOrderId(new Random().nextInt());
            orderRepository.save(orderEntity);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "Order Saved Successfully";
    }

    @Override
    public Order findByOrderId(Integer id) {
        OrderEntity orderEntity = this.orderRepository.findByOrderId(id);
        Order order = Converter.convertEntityToDTO(orderEntity);
        return order;
    }

    @Override
    public List<Order> findOrdersByName(String name) {
        List<OrderEntity> orderEntityList = this.orderRepository.findByName(name);
        List<Order> orders = new ArrayList<>();
        orderEntityList.stream().forEach(orderEntity -> {
            orders.add(Converter.convertEntityToDTO(orderEntity));
        });
        return orders;
    }
}
