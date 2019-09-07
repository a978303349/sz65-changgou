package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.changgou.feign"})
@MapperScan(basePackages = {"com.changgou.dao"})
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }
}
