package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.entity.TokenDecode;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;


    /****
     * 获取用户购物车集合
     */
    @GetMapping(value = "/list")
    public Result<List<OrderItem>> list(){
        String username = tokenDecode.getUserInfo().get("username");
        List<OrderItem> orderItems = cartService.list(username);
        return new Result<List<OrderItem>>(true,StatusCode.OK,"查询成功！",orderItems);
    }

    /**
     * 加入购物车
     * @param num 加入购物车
     * @param id  购买数量
     * @return
     */
    @RequestMapping("/add")
    public Result add(Integer num,Long id){
        //用户名
        String username = tokenDecode.getUserInfo().get("username");
        //将商品加入购物车
        cartService.add(num,id,username);
        return new Result(true, StatusCode.OK,"加入购物车成功!");
    }
}
