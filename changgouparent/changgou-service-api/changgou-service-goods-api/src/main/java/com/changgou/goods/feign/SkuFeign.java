package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {

    /***
     * 库存递减
     * @param username
     * @return
     */
    @PostMapping(value = "/decr/count")
    Result decrCount(@RequestParam(value = "username") String username);

    /***
     * 根据ID查询SKU信息
     * @param id : sku的ID
     */
    @GetMapping(value = "/{id}")
    Result<Sku> findById(@PathVariable(value = "id", required = true) Long id);

    /**
     * 根据条件搜索
     * @param sku
     * @return
     */
    @PostMapping(value = "/search" )
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

    /**
     * 根据状态进行搜索
     * @param status
     * @return
     */

    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable("status") String status);
}
