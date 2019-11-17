package com.yqy.springbootTemplate.common.interceptor;


import com.yqy.springbootTemplate.common.annotation.auth.PassToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * @program: smartpage-springboot
 * @description: 身份验证拦截器
 * @author: Mr.Yqy
 * @create: 2019-05-13 13:34
 **/
@Slf4j
@Component
public class ManageSystemInterceptor implements HandlerInterceptor {


    @Resource
    RedisTemplate redisTemplate;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        // 从 http 请求头中取出 token
        Thread.sleep(1000);
        String token = request.getHeader("Authorization");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查有没有需要用户权限的注解
        if(method.isAnnotationPresent(PassToken.class)){
            return true;
        }
            // 执行认证
            if (token == null) {
                throw new RuntimeException("请登录");
            }


            if(redisTemplate.opsForValue().get(token)==null){
                throw new RuntimeException("登陆已过期，请重新登录");
            }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) {
    }



}


