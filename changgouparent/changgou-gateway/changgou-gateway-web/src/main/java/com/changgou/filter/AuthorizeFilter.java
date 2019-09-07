package com.changgou.filter;

import com.changgou.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/*****
 * @Author: www.itheima.com
 * @Description: com.changgou.filter
 * 全局过滤器
 ****/
//@Component       // Jdk Proxy
@Configuration   //配置类 ->给对象创建实例->CglibProxy
public class AuthorizeFilter implements GlobalFilter, Ordered {

    /****
     * 令牌参数名字
     * 1:头文件中
     * 2:参数中
     * 3:Cookie
     */
    public static final String AUTHORIZE_TOKEN = "Authorization";

    //用户登录地址
    private static final String USER_LOGIN_URL="http://localhost:9001/oauth/login";

    /****
     * 全局拦截操作
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //Request
        ServerHttpRequest request = exchange.getRequest();
        //Response
        ServerHttpResponse response = exchange.getResponse();

        //获取用户请求的地址
        String uri = request.getURI().getPath();

        //鉴权->简单的鉴权[用户登录无需鉴权]   /api/user/loigin
        if(!URLFilter.hasAuthorize(uri)){
            return chain.filter(exchange);
        }

        //其他地址，需要鉴权->1)从请求头中获取Authorization令牌数据
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        //其他地址，需要鉴权->2)请求头中没有令牌数据，则从参数中回去Authorization令牌数据
        if(StringUtils.isEmpty(token)){
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }

        //其他地址，需要鉴权->3)请求参数中没有Authorization令牌数据，则从Cookie中获取
        if(StringUtils.isEmpty(token)){
            HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(cookie!=null){
                token = cookie.getValue();
            }
        }
        //如果以上获取途径都无法获取令牌，则拒绝访问
        if(token==null){
            //拦截
         //   response.setStatusCode(HttpStatus.UNAUTHORIZED);    //状态码
         //   return response.setComplete();    //结束流程
            return needAuthorization(USER_LOGIN_URL+"?FROM="+request.getURI(),exchange);
        }

        //如果有，并且能解析，则放行
        try {
            //解析令牌
          //  Claims claims = JwtUtil.parseJWT(token);

            //手动添加一些头信息
            request.mutate().header(AUTHORIZE_TOKEN,token);
        } catch (Exception e) {
            e.printStackTrace();

            //拦截
            response.setStatusCode(HttpStatus.UNAUTHORIZED);    //状态码
            return response.setComplete();    //结束流程
        }

        //放行
        return chain.filter(exchange);
    }

    /**
     * 设置响应
     * @return
     */
    private Mono<Void> needAuthorization(String utl,ServerWebExchange exchange) {
        ServerHttpResponse response=exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location",utl);
        return exchange.getResponse().setComplete();
    }

    /***
     * 过滤器的执行顺序
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
