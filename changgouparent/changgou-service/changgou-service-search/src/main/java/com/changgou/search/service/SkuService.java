package com.changgou.search.service;

import java.util.Map;

public interface SkuService {
    /**
     * 导入sku数据
     */
    void importSku();

    /**
     * 多条件搜索
     * @param search
     * @return
     */
    Map<String,Object>search(Map<String,String>search);
}
