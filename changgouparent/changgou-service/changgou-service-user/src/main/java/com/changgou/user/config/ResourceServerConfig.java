package com.changgou.user.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //公钥
    private static final String PUBLIC_KEY="public.key";

    /**
     * 定义JwtAccessTokenConverter
     * @param jwtAccessTokenConverter
     * @return
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /**
     * 定义一个JwtAccessTokenConverter
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(getPublic());
        return converter;
    }

    private String getPublic() {
        Resource resource=new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader=new InputStreamReader(resource.getInputStream());
            BufferedReader br=new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * HTTP 安全配置,对于每个到达系统的http请求连接进行校验
     * @param http
     * @throws Exception
     */
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user/add","/user/load/*")
                .permitAll()
                .anyRequest()
                .authenticated();  //其他地址需要认证授权
    }
}
