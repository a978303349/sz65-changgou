package com.changgou.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = {"${mq.pay.queue.order}"})
public class OrderPayMessageListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void consumeMessage(String msg){
        //将数据转成Map
        Map<String,String> result = JSON.parseObject(msg, Map.class);
        String return_code = result.get("return_code");
        //业务结果
        String result_code = result.get("result_code");

        //业务结果result_code=SUCCESS/FAIL 修改订单状态
        if (return_code.equalsIgnoreCase("out_trade_no")){
            //获取订单号
            String outtradeno = result.get("out_trade_no");
            //业务结果
            if (result_code.equalsIgnoreCase("success")){
                if (outtradeno!=null){
                    orderService.updateStatus(outtradeno,result.get("transaction_id"));
                }else {
                    //订单删除
                    orderService.deleteOrder(outtradeno);
                }
            }
        }
    }
}
