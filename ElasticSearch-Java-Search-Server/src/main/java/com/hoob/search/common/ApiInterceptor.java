package com.hoob.search.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.hoob.search.redis.route.RedisInitUtil;

/***
 * Spring Boot 拦截器配置
 */
@Configuration
public class ApiInterceptor extends WebMvcConfigurationSupport{
	@Bean
    public SigarCollection sigarCollection(){
		SigarCollection multipartResolver = new SigarCollection();
        return  multipartResolver;
    }
	@Bean(initMethod="init")
    public RedisInitUtil jedisPoolConfig(){
		RedisInitUtil redisInitUtil = new RedisInitUtil();
		return redisInitUtil;
    }
}
