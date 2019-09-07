package changgou.service;

import java.util.Map;

public interface WeixinPayService {
    /**
     * 创建二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    Map createNative(String out_trade_no,String total_fee);

    /**
     * 查询订单状态
     * @param out_trade_no
     * @return
     */
    Map queryPayStatus(String out_trade_no);

    Map<String, String> createNative(Map<String, String> parameters)throws  Exception;
}
