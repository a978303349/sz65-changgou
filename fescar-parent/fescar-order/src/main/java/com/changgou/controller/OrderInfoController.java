package com.changgou.controller;

import com.changgou.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderInfo")
@CrossOrigin
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 增加订单
     * @param username
     * @param id
     * @param count
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestParam("name")String username,@RequestParam("id")int id,@RequestParam("count")int count){
        //添加订单
        orderInfoService.add(username,id,count);
        return "success";
    }
}
