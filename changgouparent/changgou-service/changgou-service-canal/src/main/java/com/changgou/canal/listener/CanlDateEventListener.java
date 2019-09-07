package com.changgou.canal.listener;


import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.changgou.entity.Result;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@CanalEventListener
public class CanlDateEventListener {

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 修改广告数据监听
     * 同步数据到redis
     * @param eventType
     * @param rowData
     */
    @ListenPoint(destination = "example",schema = "changgou_content",table = {"tb_content"},eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE})
    public void onEventCustomUpate(CanalEntry.EventType eventType,CanalEntry.RowData rowData) {
        //获取广告分类的id
        String categoryId = getColumn(rowData, "category_id");
        //根据广告分类id获取所有广告
        Result<List<Content>> result = contentFeign.findByCategory(categoryId);
        //将广告存入redis
        if (result.getData() != null) {
            stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(result.getData()));
        }
    }

    /**
     * 获取指定列的值
     * @param rowData
     * @param category_id
     * @return
     */
    private String getColumn(CanalEntry.RowData rowData, String columnName) {
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            if (column.getName().equals(columnName)) {
                return column.getValue();
            }
        }
        //有可能是删除操作
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            if (column.getName().equals(columnName)) {
                return column.getValue();
            }
        }
        return null;
    }

    /******
     * 增加数据监听
     * eventType:操作类型  增加
     * rowData:当前监控的变化数据
     */
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        //rowData.getAfterColumnsList():发生变更后的数据
        //rowData.getBeforeColumnsList():数据发生变更前的记录
        System.out.println("============增加监控============");
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println(column.getName() + ":" + column.getValue());
        }
    }

    /**
     * 修改数据监听
     * @param rowData
     */
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.RowData rowData) {
        System.out.println("UpdateListenPoint");
        rowData.getAfterColumnsList().forEach((c) -> System.out.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
    }

    /**
     * 删除监听
     * @param eventType
     */
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        System.out.println("===========删除监控==========");
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println(column.getName() + ":" + column.getValue());
        }
    }

    /***
     * 自定义数据监控
     * destination = example:监控的实例指定
     * schema = "":指定监控的数据库
     * table = {"tb_content"}:指定监控的表
     * @ListenPoint(schema = "changgou_content",table = {"tb_content"},):指定监控类型
     */
    @ListenPoint(destination ="example",schema = "changgou_content",table = {"tb_content_category", "tb_content"},eventType = CanalEntry.EventType.DELETE)
    public void onEventCustomUpdate(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        System.out.println("--------AAAA------删除监控------AAAA------");
        rowData.getBeforeColumnsList().forEach(c ->
                System.out.println("By--Annotation:"+c.getName()+":"+c.getValue()));
    }
}
