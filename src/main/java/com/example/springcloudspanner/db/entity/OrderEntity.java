package com.example.springcloudspanner.db.entity;

import lombok.ToString;
import org.springframework.cloud.gcp.data.spanner.core.mapping.*;
import lombok.Getter;
import lombok.Setter;


@Table(name="Orders")
@Setter
@Getter
@ToString
public class OrderEntity {

    @PrimaryKey
    @Column(name="orderId")
    private int orderId;

    private String name;

    private String order_status;

}
