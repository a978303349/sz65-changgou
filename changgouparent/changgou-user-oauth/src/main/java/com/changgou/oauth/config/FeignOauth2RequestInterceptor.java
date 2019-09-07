package com.changgou.oauth.config;

import com.changgou.oauth.util.JwtToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
@Configuration
public class FeignOauth2RequestInterceptor implements RequestInterceptor {

    /****
     * 自定义操作
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {

        //创建令牌信息
        String token = "Bearer " + JwtToken.adminJwt();
        //将令牌添加到头文件中
        requestTemplate.header("Authorization", token);

    }
}