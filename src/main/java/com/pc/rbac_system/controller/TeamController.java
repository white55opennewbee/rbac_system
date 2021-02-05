package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Team;
import com.pc.rbac_system.service.IStudentService;
import com.pc.rbac_system.service.ITeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAuthority('wx:student:read')")
@RequestMapping("/team")
public class TeamController {
    @Autowired
    IStudentService studentService;
    @Autowired
    ITeamService teamService;

    @GetMapping("/findAll/{teacherId}/{currentPage}/{maxSize}")
    public Result findAllTeam(@PathVariable Long teacherId,@PathVariable Integer currentPage,@PathVariable Integer maxSize){
       return teamService.findAllTeam(currentPage,maxSize,teacherId);
    }

    @PostMapping("/addTeam/{teacherId}")
    public Result addTeam(@RequestBody Team team,@PathVariable Long teacherId ){
       return teamService.addTeam(team,teacherId);
    }

    @DeleteMapping("/deleteTeam/{teamId}")
    public Result deleteTeam(@PathVariable("teamId")Long teamId){
        return teamService.deleteTeam(teamId);
    }

    @PostMapping("/updateTeam")
    public Result updateTeam(@RequestBody Team team){
        return teamService.updateTeam(team);
    }


    @GetMapping("/findTeamById/{id}")
    public Result findTeamById(@PathVariable Long id){
        return teamService.findTeamById(id);
    }


}
