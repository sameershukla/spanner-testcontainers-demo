package com.example.springcloudspanner.db.repository;

import com.example.springcloudspanner.db.entity.OrderEntity;
import com.example.springcloudspanner.model.Order;
import org.springframework.stereotype.Repository;
import org.springframework.cloud.gcp.data.spanner.repository.*;
import java.util.List;

@Repository
public interface OrderRepository extends SpannerRepository<OrderEntity, Long> {

    public List<OrderEntity> findByName(String name);

    public OrderEntity findByOrderId(Integer orderId);
}
