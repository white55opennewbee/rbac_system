package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.JwtTokenUtil;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.User;
import com.pc.rbac_system.service.IRedisService;
import com.pc.rbac_system.service.IUserService;
import com.pc.rbac_system.vo.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class LoginAndRegisterController {
    @Autowired
    IUserService service;
    @Autowired
    UserDetailsService userDetailsService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginParam loginParam){
        Result result = null;
        try {
            result = service.findUserByUsernameAndCheckPassword(loginParam);
        } catch (IOException e) {
            Result.error(CodeMsg.SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        User user1 = service.addUser(user);
        return Result.success(user1);
    }

}
