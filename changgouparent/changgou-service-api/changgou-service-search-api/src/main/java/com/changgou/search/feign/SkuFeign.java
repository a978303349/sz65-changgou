package com.changgou.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name="search")
@RequestMapping("/search")
public interface SkuFeign {

    /**
     * @param searchMap
     * @return
     * @GetMapping:
     * @RequestParam(required = false):用户可以不传任何参数
     */
    @GetMapping
    Map<String, Object> search(@RequestParam(required = false) Map<String, String> searchMap);
}