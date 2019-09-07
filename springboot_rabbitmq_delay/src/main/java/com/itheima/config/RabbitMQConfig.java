package com.itheima.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*****
 * @Author: www.itheima.com
 * @Description: com.itheima.config
 ****/
@Configuration
public class RabbitMQConfig {


    /*****
     * 过期队列  queue1
     * DirectExchange
     */
    @Bean
    public Queue delayMessageQueue(){
        return QueueBuilder.durable("delayMessageQueue")
                .withArgument("x-dead-letter-exchange","delayExchange") //当前过期队列过期了，消息转发给指定的交换机
                .withArgument("x-dead-letter-routing-key","messageQueue") //当前交换机会将队列路由给指定的队列
                .build();
    }


    /****
     * 接收消息的队列  queue2
     */
    @Bean
    public Queue messageQueue(){
        return new Queue("messageQueue");
    }

    /****
     * 创建交换机
     */
    @Bean
    public Exchange delayExchange(){
        return new DirectExchange("delayExchange",true,false);
    }


    /***
     * queue2队列绑定交换机
     */
    @Bean
    public Binding messageBindingExchange(Queue messageQueue,Exchange delayExchange){
        return BindingBuilder.bind(messageQueue).to(delayExchange).with("messageQueue").noargs();
    }

}
