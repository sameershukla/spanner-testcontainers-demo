package com.example.springcloudspanner.model;

import com.example.springcloudspanner.db.entity.OrderEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
//@Transactional
public class Converter {

    public static OrderEntity convertDTOToEntity(Order order){
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(order, orderEntity);
        return orderEntity;
    }

    public static Order convertEntityToDTO(OrderEntity orderEntity){
        Order order = new Order();
        BeanUtils.copyProperties(orderEntity, order);
        return order;
    }
}
