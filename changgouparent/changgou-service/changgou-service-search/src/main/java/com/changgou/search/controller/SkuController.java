package com.changgou.search.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 导入数据
     * @return
     */
    @GetMapping("/import")
    public Result search(){
        skuService.importSku();
        return new Result(true, StatusCode.OK,"导入数据到索引库中成功");
    }

    /**
     * @GetMapping:
     * @param searchMap
     * @RequestParam(required = false):用户可以不传任何参数
     * @return
     */
    @GetMapping
    public Map<String,Object>search(@RequestParam(required = false)Map<String,String> searchMap){
        //掉用搜索
        Map<String, Object> resultMap = skuService.search(searchMap);
        return resultMap;
    }
}


