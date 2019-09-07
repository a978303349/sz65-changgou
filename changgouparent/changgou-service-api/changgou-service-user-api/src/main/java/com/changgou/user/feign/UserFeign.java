package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/****
 * @Author:liaoyanglin
 * @Description:
 * @Date 2019/6/18 13:58
 *****/
@FeignClient(name="user")
@RequestMapping("/user")
public interface UserFeign {
    /***
     * 添加用户积分
     * @param points
     * @return
     */
    @GetMapping(value = "/points/add")
    Result addPoints(@RequestParam(value = "points")Integer points);

    /***
     * 根据ID查询User数据
     * @param id
     * @return
     */
    @GetMapping("/load/{id}")
    Result<User> findById(@PathVariable("id") String id);

}
