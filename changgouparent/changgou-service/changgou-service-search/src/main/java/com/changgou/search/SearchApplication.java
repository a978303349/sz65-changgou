package com.changgou.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
//开启Feign
@EnableFeignClients(basePackages = "com.changgou.goods.feign")
//开启es并指定esDao包路径
@EnableElasticsearchRepositories(basePackages = "com.changgou.search.dao")
public class SearchApplication {  /**
 * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
 * 解决netty冲突后初始化client时还会抛出异常
 * availableProcessors is already set to [12], rejecting [12]
 ***/
    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.properties","false");
        SpringApplication.run(SearchApplication.class,args);
    }
}
