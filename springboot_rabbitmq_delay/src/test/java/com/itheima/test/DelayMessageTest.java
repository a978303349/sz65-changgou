package com.itheima.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*****
 * @Author: www.itheima.com
 * @Description: com.itheima.test
 * 延时消息发送
 ****/
@RunWith(SpringRunner.class)
@SpringBootTest
public class DelayMessageTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /****
     * 发送延时队列测试
     */
    @Test
    public void testSendDelayMessage() throws IOException {
        rabbitTemplate.convertAndSend("delayMessageQueue", (Object) "hello!", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置消息10秒过期
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("发送消息时间:"+simpleDateFormat.format(new Date()));
        System.in.read();
    }

}
