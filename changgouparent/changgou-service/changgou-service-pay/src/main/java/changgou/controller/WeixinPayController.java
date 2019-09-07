package changgou.controller;

import changgou.service.WeixinPayService;
import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.github.wxpay.sdk.WXPayUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

@RestController
@RequestMapping("/weixin/pay")
@CrossOrigin
public class WeixinPayController {

    @Value("${mq.pay.exchange.order}")
    private String exchange;
    @Value("${mq.pay.queue.order}")
    private String queue;
    @Value("${mq.pay.routing.key}")
    private String routing;

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 支付回调
     * @param request
     * @return
     */
    @RequestMapping("/notify/url")
    public String notifyUrl(HttpServletRequest request) throws Exception {
        ServletInputStream is = request.getInputStream();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;
        while ((len=is.read(buffer))!=-1){
            os.write(buffer,0,len);
        }
        os.close();
        os.flush();
        is.close();
        //将支付回调数据转成xml字符串
        String result=new String(os.toByteArray(),"utf-8");
        //将xml字符串转成map结构
        Map<String, String> map = WXPayUtil.xmlToMap(result);

        //获取附加信息
        Map attach = JSON.parseObject(map.get("attach"), Map.class);
        System.out.println(attach);

        //将消息发送给RabbitMQ
        rabbitTemplate.convertAndSend(JSON.toJSONString(map), exchange, attach.get("queue"));

        //响应数据设置
        Map resultMap=new HashMap();
        resultMap.put("return_code","SUCCESS");
        resultMap.put("return_msg","OK");
        return WXPayUtil.mapToXml(resultMap);
    }
    /**
     * 查询支付状态
     * @param outtradeno
     * @return
     */
    @GetMapping("/status/query")
    public Result queryStatus(String outtradeno){
        Map<String,String> resultmap = weixinPayService.queryPayStatus(outtradeno);
        return new Result(true,StatusCode.OK,"查询状态成功",resultmap);
    }

    /****
     * 创建二维码支付
     * @return
     */
    @RequestMapping(value = "/create/native")
    //public Result<Map> createNative(String outtradeno, String money) throws Exception{
    public Result<Map> createNative(@RequestParam Map<String,String> parameters) throws Exception{
        //Map<String, String> resultMap = weixinPayService.createNative(outtradeno, money);
        Map<String, String> resultMap = weixinPayService.createNative(parameters);
        return new Result<Map>(true, StatusCode.OK,"创建支付二维码成功！",resultMap);
    }
}