package com.changgou.service;

public interface ItemInfoService {
    /**
     * 递减库存
     * @param id
     * @param count
     */
    void decrCount(int id,int count);
}
