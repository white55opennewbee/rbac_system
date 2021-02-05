package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coach")
public class CoachController {
    @Autowired
    IUserService userService;

    @GetMapping("/findAllCoachs")
    public Result findAllCoachs(){
        return userService.findusersByRole("教练");
    }
}
