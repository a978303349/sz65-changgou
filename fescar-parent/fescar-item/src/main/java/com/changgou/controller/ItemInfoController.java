package com.changgou.controller;

import com.changgou.service.ItemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itemInfo")
@CrossOrigin
public class ItemInfoController {

    @Autowired
    private ItemInfoService itemInfoService;


    @PostMapping("/decrCount")
    public String decrCount(@RequestParam("id") int id,@RequestParam("count") int count){
        //库存递减
        itemInfoService.decrCount(id,count);
        return "success";
    }

}
