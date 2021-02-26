package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Team;
import com.pc.rbac_system.service.IStudentService;
import com.pc.rbac_system.service.ITeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAuthority('wx:team:read')")
@RequestMapping("/team")
public class TeamController {
    @Autowired
    IStudentService studentService;
    @Autowired
    ITeamService teamService;

    @GetMapping("/findAll/{teacherId}/{currentPage}/{maxSize}")
    @PreAuthorize("hasAuthority('wx:team:read')")
    public Result findAllTeam(@PathVariable Long teacherId,@PathVariable Integer currentPage,@PathVariable Integer maxSize){
       return teamService.findAllTeam(currentPage,maxSize,teacherId);
    }

    @PostMapping("/addTeam/{teacherId}")
    @PreAuthorize("hasAuthority('wx:team:create')")
    public Result addTeam(@RequestBody Team team,@PathVariable Long teacherId ){
       return teamService.addTeam(team,teacherId);
    }

    @DeleteMapping("/deleteTeam/{teamId}")
    @PreAuthorize("hasAuthority('wx:team:delete')")
    public Result deleteTeam(@PathVariable("teamId")Long teamId){
        return teamService.deleteTeam(teamId);
    }

    @PostMapping("/updateTeam")
    @PreAuthorize("hasAuthority('wx:team:update')")
    public Result updateTeam(@RequestBody Team team){
        return teamService.updateTeam(team);
    }

    @PutMapping("/setTeamLeader/{stuId}/{teamId}")
    @PreAuthorize("hasAuthority('wx:team:teamleader')")
    public Result setTeamLeader(@PathVariable Long stuId,@PathVariable Long teamId){
       return teamService.setTeamLeader(stuId,teamId);
    }


    @GetMapping("/findTeamById/{id}")
    @PreAuthorize("hasAnyRole('班主任','教练','组长')")
    public Result findTeamById(@PathVariable Long id){
        return teamService.findTeamById(id);
    }


}
