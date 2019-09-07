package com.changgou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "${mq.pay.queue.seckillorder}")
public class SeckillOrderPayMessageListener {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @RabbitHandler
    public void consumeMessage(@Payload String message){
        System.out.println(message);
        //将消息转成map对象
        Map<String,String>resultMap = JSON.parseObject(message, Map.class);
        String username = resultMap.get("username");
        System.out.println("监听到的消息:"+resultMap);

        //返回状态码
        String returnCode = resultMap.get("return_code");
        String resultCode = resultMap.get("result_code");

        String outtradeno = resultMap.get("out_trade_no");

        //支付成功
        if(resultCode.equalsIgnoreCase("success")){
            //修改订单状态
            seckillOrderService.updatePayStatus(outtradeno,resultMap.get("transaction_id"),resultMap.get("username"));
        }else {
            //支付失败,删除订单
            seckillOrderService.closeOrder(username);
        }

    }
}
