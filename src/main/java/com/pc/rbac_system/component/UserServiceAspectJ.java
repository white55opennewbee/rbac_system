package com.pc.rbac_system.component;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.EsLoginInfo;
import com.pc.rbac_system.model.User;
import com.pc.rbac_system.service.IESLoginInfoService;
import com.pc.rbac_system.service.IUserService;
import com.pc.rbac_system.vo.LoginParam;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@EnableAspectJAutoProxy
@Aspect
public class UserServiceAspectJ {
    @Autowired
    IUserService userService;
    @Autowired
    IESLoginInfoService esLoginInfoService;
    @Autowired
    PasswordEncoder passwordEncoder;


    // 切面，登录后将登录记录写入elasticsearch中
    @AfterReturning(value = "execution(* com.pc.rbac_system.controller.LoginAndRegisterController.login(..))", returning = "result")
    public void esLoginInfoAdd(JoinPoint joinPoint, Result result) throws IOException {
        if (result.getCode() == 201) {
            Object[] args = joinPoint.getArgs();
            LoginParam params = (LoginParam) args[0];
            User userByUsername = userService.findUserByUsername(params.getUsername());
            //在elasticsearch中存储登录记录
            EsLoginInfo loginInfo = new EsLoginInfo();
            BeanUtils.copyProperties(params, loginInfo);
//            loginInfo.setIp(param.getIp());
//            loginInfo.setAddr(param.getAddr());
//            loginInfo.setUsername(param.getUsername());
            loginInfo.setLoginTime(new Date(System.currentTimeMillis()));
            loginInfo.setId(userByUsername.getId());
            loginInfo.setNickName(userByUsername.getNickName());
            esLoginInfoService.putEsLogin(loginInfo);
        }
    }
}
