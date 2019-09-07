package com.changgou.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.item.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*****
 * @Author: www.itheima.com
 * @Description: com.changgou.item.service.impl
 ****/
@Service
public class PageServiceImpl implements PageService {

    /***
     * 用它生成静态页
     */
    @Autowired
    private TemplateEngine templateEngine;

    //查询Spu
    @Autowired
    private SpuFeign spuFeign;

    //Spu对应的List<Sku>
    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    /***
     * 查询Spu、List<Sku>、三个分类信息
     * @param spuId
     */
    public Map<String,Object> buildDataModel(Long spuId){
        //查询Spu
        Result<Spu> spuResult = spuFeign.findById(spuId);
        Spu spu = spuResult.getData();

        //查询三个分类
        Result<Category> category1Result = categoryFeign.findById(spu.getCategory1Id());
        Result<Category> category2Result = categoryFeign.findById(spu.getCategory2Id());
        Result<Category> category3Result = categoryFeign.findById(spu.getCategory3Id());

        //List<Sku>
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        Result<List<Sku>> skuListResult = skuFeign.findList(sku);

        //创建Map存储所有数据
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("spu",spu);
        dataMap.put("category1",category1Result.getData());
        dataMap.put("category2",category2Result.getData());
        dataMap.put("category3",category3Result.getData());
        dataMap.put("skuList",skuListResult.getData());
        //处理图片
        dataMap.put("iamges",spu.getImages().split(","));//图片
        dataMap.put("specificationList", JSON.parseObject(spu.getSpecItems(),Map.class));
        //{"电视音响效果":["小影院","环绕","立体声"],"电视屏幕尺寸":["20英寸","60英寸","50英寸"],"尺码":["165","170","175"]}
        //spec_items
        return dataMap;
    }


    /***
     * 生成静态页
     * @param spuid
     */
    @Override
    public void createHtml(Long spuid) {
        try {
            //创建一个容器对象，用于存储页面所需的变量信息  Context
            Context context = new Context();

            //查询所需数据
            Map<String, Object> dataMap = buildDataModel(spuid);
            context.setVariables(dataMap);

            //创建一个Writer对象，并指定生成的静态页文件全路径
            FileWriter fileWriter = new FileWriter("D:/items/"+spuid+".html");

            /***
             * 执行生成操作
             * 1:指定模板
             * 2:模板所需的数据模型
             * 3:输出文件对象(文件生成到哪里去)
             */
            templateEngine.process("item",context,fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
