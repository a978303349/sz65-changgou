package changgou.service.impl;

import changgou.service.WeixinPayService;
import com.alibaba.fastjson.JSON;
import com.changgou.entity.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;

    @Value("${weixin.notifyurl}")
    private String notifyurl;

    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        return null;
    }

    /**
     * 查询订单状态
     * @param out_trade_no  客户端自定义订单编号
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {

        try {
            //1.封装参数
            Map param=new HashMap();
            param.put("appid",appid);
            //应用id
            param.put("mch_id",partner);
            //商户号
            param.put("out_trade_no",out_trade_no);
            //商户订单编号
            param.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符
            //2.将参数转成xml字符,并携带签名
            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);

            //3.发送请求
            HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();

            //4.获取返回值,并将返回值转成Map
            String content = httpClient.getContent();
            return WXPayUtil.xmlToMap(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /***
     * 公众账号ID	appid
     * 商户号	mch_id
     * 随机字符串	nonce_str
     * 签名	sign
     * 商品描述	body
     * 商户订单号	out_trade_no[就是商品订单号]
     * 标价金额	total_fee
     * 终端IP	spbill_create_ip
     * 通知地址	notify_url
     * 交易类型	trade_type
     *  outtradeno:订单号
     *  totalfee:支付金额
     */
    @Override
    //public Map<String, String> createNative(String outtradeno, String totalfee) throws Exception{
    public Map<String, String> createNative(Map<String,String> parameters) throws Exception{
        //准备请求地址
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        //准备提交的参数
        Map<String,String> mapParameters = new HashMap<String,String>();
        mapParameters.put("appid",appid);        //应用ID
        mapParameters.put("mch_id",partner);       //商户ID
        mapParameters.put("nonce_str", WXPayUtil.generateNonceStr());        //随机数
        mapParameters.put("body","畅购最牛的商品！");
        mapParameters.put("out_trade_no",parameters.get("outtradeno"));   //订单号
        mapParameters.put("total_fee",parameters.get("totalfee"));          //支付金额，单位：分
        mapParameters.put("spbill_create_ip","127.0.0.1");
        mapParameters.put("notify_url",notifyurl);
        mapParameters.put("trade_type","NATIVE");

        //附加参数  attach 队列名称
        String exchange = parameters.get("exchange");
        String routingkey = parameters.get("routingkey");
        String username = parameters.get("username");
        Map<String,String> attachMap = new HashMap<String,String>();
        attachMap.put("exchange",exchange);
        attachMap.put("routingkey",routingkey);
        if(!StringUtils.isEmpty(username)){
            attachMap.put("username",username);
        }
        mapParameters.put("attach", JSON.toJSONString(attachMap));  //127

        //生成XML参数，并携带签名
        String xmlParameters = WXPayUtil.generateSignedXml(mapParameters, partnerkey);

        //是否使用Https
        HttpClient httpClient = new HttpClient(url);
        httpClient.setHttps(true);

        //将数据传输过去
        httpClient.setXmlParam(xmlParameters);

        //执行提交->GET/POST
        httpClient.post();

        //获取结果集
        String content = httpClient.getContent();

        //将返回的结果转成Map
        return WXPayUtil.xmlToMap(content);
    }
}
