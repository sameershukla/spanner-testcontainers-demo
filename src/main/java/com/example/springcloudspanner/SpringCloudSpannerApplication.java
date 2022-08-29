package com.example.springcloudspanner;

import com.example.springcloudspanner.model.Order;
import com.example.springcloudspanner.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class SpringCloudSpannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudSpannerApplication.class, args);
	}

}
