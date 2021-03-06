package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.service.ISignService;
import com.pc.rbac_system.vo.SignSearchParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
public class SignController {
    @Autowired
    ISignService signService;

    @GetMapping("/findTodaySignByTeam/{teamId}/{currentPage}/{maxSize}")
    @PreAuthorize("hasAuthority('wx:student:clock:read')")
    public Result findTodaySign(@PathVariable Long teamId,@PathVariable Integer currentPage,@PathVariable Integer maxSize){
       return signService.findTodaySignByTeamId(teamId,currentPage,maxSize);
    }

    @PostMapping(path = {"/markStudentSignByTeamId/{teamId}/{studentId}/{signIn}/{delayTime}","/markStudentSignByTeamId/{teamId}/{studentId}/{signIn}"})
    @PreAuthorize("hasRole('组长')")
    public Result markStudentSignByTeamId(@PathVariable Long teamId, @PathVariable Long studentId, @PathVariable Integer signIn,@PathVariable(required = false) String delayTime){
        boolean b = signService.SetTodaySignType(signIn, studentId, teamId);
        return Result.success(b);
    }

    @PostMapping("/changeStudentSignType/{signId}/{signTypeId}")
    public Result changeStudentSignType(@PathVariable Long signTypeId, @PathVariable Long signId){
        boolean b = signService.changeStudentSignType(signTypeId, signId);
        return Result.success(b);
    }

    @GetMapping("/findAllSignStatus")
    public Result findAllSignStatus(){
        return Result.success(signService.findAllSignStatus());
    }

    @PostMapping("/findSignsRecordByPage/{teacherId}")
    @PreAuthorize("hasAnyRole('班主任')")
    public Result findSignsRecordByPage(@RequestBody SignSearchParam signSearchParam,@PathVariable Long teacherId){
       return Result.success(signService.findSignsRecordByPage(signSearchParam,teacherId));
    }

}
