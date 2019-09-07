package com.changgou.seckill.task;

import com.changgou.entity.DateUtil;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Component
public class SeckillGoodsPushTask {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 每30秒执行一次
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis(){
        //获取时间段集合
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date startTime : dateMenus) {
            //命名空间获取
            String namespace ="SeckillGoods_"+ DateUtil.data2str(startTime,DateUtil.PATTERN_YYYYMMDDHH);

            //根据时间段数据查询对应的秒杀商品数据
            Example example=new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            //商品必须审核通过
            criteria.andEqualTo("status","1");
            //库存>0
            criteria.andGreaterThan("stockCount",0);
            //开始时间<=活动时间
            criteria.andLessThanOrEqualTo("startTime",startTime);
            //活动结束时间<开始时间+2小时
            criteria.andLessThan("endTime",DateUtil.addDateHour(startTime,2));
            //排除之前已经加载到redis缓存中商品数据
            Set keys = redisTemplate.boundHashOps(namespace).keys();
            if (keys!=null&&keys.size()>0){
                criteria.andNotIn("id",keys);
            }
            //查询数据
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);

          //  System.out.println(seckillGoods.size());
            //将秒杀商品数据存入redis缓存中
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps(namespace).put(seckillGood.getId(),seckillGood);

                //商品个数队列创建 seckillGood.getStockCount=5
                redisTemplate.boundListOps("SeckillGoodsCountList_"+seckillGood.getId()).leftPushAll(pushIds(seckillGood.getId(),seckillGood.getStockCount()));
            }
        }
    }

    private Long[] pushIds(Long id, Integer count) {
        Long[] ids=new Long[count];
        for (int i = 0; i < ids.length; i++) {
            ids[i]=id;
        }
        return ids;
    }
}
