package com.changgou.search.controller;


import com.changgou.entity.Page;
import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(value = "/search")
public class SkuController {


    @Autowired
    private SkuFeign skuFeign;

    @GetMapping("/list")
    public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model){

        //替换特殊字符
        headlerSearchMap(searchMap);
        //调用changgou-service-search微服务
        Map<String,Object> result = skuFeign.search(searchMap);
        //搜索数据结果
        model.addAttribute("result",result);
        //搜索条件
        model.addAttribute("searchMap",searchMap);
        //请求地址
        String[] urls=url(searchMap);
        model.addAttribute("url",urls[0]);
        model.addAttribute("sorturl",urls[1]);
        //分页计算
        Page page = new Page(
                Long.parseLong(result.get("totalPages").toString()),//总记录数
                Integer.parseInt(result.get("pageNum").toString()), //当前页
                Integer.parseInt(result.get("pageSize").toString()));//每页显示条数
        model.addAttribute("page",page);
        return "search";
    }

    /**
     * url封装处理
     * @param searchMap
     * @return
     */
    private String[] url(Map<String, String> searchMap) {
        //URL地址
        String url="/search/list";
        String sorturl="/search/list";
        if (searchMap!=null){
            url+="?";
            sorturl+="?";
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key=entry.getKey();      //brand
                String value = entry.getValue();//华为

                if (entry.getKey().equalsIgnoreCase("pageNum")) {
                    continue;
                }
                //
                url+=key+"="+value+"&";
            }
            //去掉最后一个&
            url=url.substring(0,url.length()-1);
            sorturl=sorturl.substring(0,sorturl.length()-1);
        }
        return new String[]{url,sorturl};
    }

    /**
     * 替换特殊字符
     * @param searchMap
     */
    public void headlerSearchMap(Map<String,String>searchMap){
        if (searchMap!=null){
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                if (entry.getKey().startsWith("spec_")){
                    entry.setValue(entry.getValue().replace("+","%2B"));
                }
            }
        }
    }
}
