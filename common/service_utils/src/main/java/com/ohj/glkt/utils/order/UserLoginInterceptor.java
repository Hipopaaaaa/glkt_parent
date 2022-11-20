package com.ohj.glkt.utils.order;


import com.ohj.glkt.utils.jwt.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j

public class UserLoginInterceptor implements HandlerInterceptor {

    private RedisTemplate redisTemplate;

    public UserLoginInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.initUserLoginVo(request);
        return true;
    }

    private void initUserLoginVo(HttpServletRequest request){
        String token = request.getHeader("token");


        log.info("前端传来的token:  {}",token);


        //String userId = request.getHeader("userId");
        if (StringUtils.isEmpty(token)) {
            request.getSession().setAttribute("userId",1L);
            log.info("token为null,往session中放入数据：{}",1L);
            AuthContextHolder.setUserId(1L);
        } else {
            //前端传来的token 后面默认带 #/ 而后端生成的token不会有。因此需要去除
            token= token.replace("#/","");
            Long userId = JwtHelper.getUserId(token);
            log.info("当前用户id： {}",userId);



            if (StringUtils.isEmpty(userId)) {
                //TODO 保存到session中
                AuthContextHolder.setUserId(1L);
                request.getSession().setAttribute("userId",userId);
                log.info("userid为null  往session中放入数据：{}",userId);
            } else {
                AuthContextHolder.setUserId(userId);
                request.getSession().setAttribute("userId",userId);
                log.info("userid不null  往session中放入数据：{}",userId);
            }
        }
    }

}
