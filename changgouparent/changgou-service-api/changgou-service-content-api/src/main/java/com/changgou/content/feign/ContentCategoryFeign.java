package com.changgou.content.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/****
 * @Author:shenkunlin
 * @Description:
 * @Date 2019/6/18 13:58
 *****/
@FeignClient(name="user")
@RequestMapping("/contentCategory")
public interface ContentCategoryFeign {

}