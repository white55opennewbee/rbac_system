package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.service.ISignService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
public class SignController {
    @Autowired
    ISignService signService;

    @GetMapping("/findTodaySignByTeam/{teamId}/{currentPage}/{maxSize}")
    public Result findTodaySign(@PathVariable Long teamId,@PathVariable Integer currentPage,@PathVariable Integer maxSize){
       return signService.findTodaySignByTeamId(teamId,currentPage,maxSize);
    }

    @PostMapping(path = {"/markStudentSignByTeamId/{teamId}/{studentId}/{signIn}/{delayTime}","/markStudentSignByTeamId/{teamId}/{studentId}/{signIn}"})
    public Result markStudentSignByTeamId(@PathVariable Long teamId, @PathVariable Long studentId, @PathVariable Integer signIn,@PathVariable(required = false) String delayTime){
        boolean b = signService.SetTodaySignType(signIn, studentId, teamId);
        return Result.success(b);
    }

}
