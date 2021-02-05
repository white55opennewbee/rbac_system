package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.service.ISignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign")
public class SignController {
    @Autowired
    ISignService signService;

    @GetMapping("/findTodaySignByTeam/{teamId}")
    public Result findTodaySign(@PathVariable Long teamId){
       return signService.findTodaySignByTeamId(teamId);
    }
}
