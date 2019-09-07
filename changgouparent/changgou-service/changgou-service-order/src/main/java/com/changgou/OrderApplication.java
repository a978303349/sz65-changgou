package com.changgou;
import com.changgou.entity.FeignInterceptor;
import com.changgou.entity.IdWorker;
import com.changgou.entity.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.changgou.goods.feign","com.changgou.user.feign"})
@MapperScan(basePackages = "com.changgou.order.dao")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }

    /**
     * 创建拦截器对象
     * @return
     */
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }

    /**
     * 创建令牌解析对象
     * @return
     */
    @Bean
    public TokenDecode tokenDecode(){
        return new TokenDecode();
    }

    /**
     * 生成订单号
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
