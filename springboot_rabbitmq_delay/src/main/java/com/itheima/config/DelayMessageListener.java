package com.itheima.config;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/*****
 * @Author: www.itheima.com
 * @Description: com.itheima.config
 * 延时队列监听
 ****/
@Component
@RabbitListener(queues = {"messageQueue"})
public class DelayMessageListener {

    @RabbitHandler
    public void getDelayMessage(String msg){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(new Date())+",读到的消息是："+msg);
    }
}
