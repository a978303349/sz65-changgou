package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

public interface CartService {

    /***
     * 购物车列表
     * @param username
     */
    List<OrderItem> list(String username);

    /**
     * 添加购物车
     * @param num  购买商品数量
     * @param id   购买id
     * @param username  购买用户
     */
    void add(Integer num,Long id,String username);
}
