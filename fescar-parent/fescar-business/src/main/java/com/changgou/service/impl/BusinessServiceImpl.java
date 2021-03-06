package com.changgou.service.impl;

import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.changgou.dao.LogInfoMapper;
import com.changgou.service.BusinessService;
import com.changgou.feign.OrderInfoFeign;
import com.changgou.feign.UserInfoFeign;
import com.changgou.pojo.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private OrderInfoFeign orderInfoFeign;

    @Autowired
    private UserInfoFeign userInfoFeign;
        @Autowired
    private LogInfoMapper logInfoMapper;

    /**
     * ①
     * @GlobalTransactional 全局事务的入口
     * 下单
     * @param username
     * @param id
     * @param count
     */
    @GlobalTransactional
    @Override
    public void add(String username, int id, int count) {
        //添加订单日志
        LogInfo logInfo=new LogInfo();
        logInfo.setContent("添加订单数据----"+new Date());
        logInfo.setCreatetime(new Date());
        int logcount = logInfoMapper.insertSelective(logInfo);
        System.out.println("添加日志受影响行:"+logcount);

        //添加订单
        orderInfoFeign.add(username,id,count);

        //用户账户余额递减
        userInfoFeign.decrMoney(username,10);
    }
}
