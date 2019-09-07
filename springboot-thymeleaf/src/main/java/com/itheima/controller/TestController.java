package com.itheima.controller;

import com.itheima.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public String hello(Model model){
         model.addAttribute("hello","hello welcome");

         //集合数据
        List<User> users = new ArrayList<User>();
        users.add(new User(1,"张三","深圳"));
        users.add(new User(2,"李四","北京"));
        users.add(new User(3,"王五","武汉"));
        return "demo1";
    }
}
