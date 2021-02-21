package com.pc.rbac_system.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.mapper.TeamMapper;
import com.pc.rbac_system.model.Team;
import com.pc.rbac_system.service.ITeamService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TeamServiceImpl implements ITeamService {
    @Autowired
    TeamMapper teamMapper;

    @Override
    public Result findAllTeam(Integer currentPage, Integer maxSize,Long teacherId) {
        PageHelper.startPage(currentPage,maxSize,true,null,true);
        List<Team> list = teamMapper.findAllTeamByTeacherId(teacherId);
        PageInfo info = new PageInfo(list);
        return Result.success(info);
    }

    @Override
    @Transactional
    public Result addTeam(Team team, Long teacherId) {
        team.setCreateTime(new Date(System.currentTimeMillis()));
        teamMapper.addTeam(team);
        Integer integer = teamMapper.addTeamAndTeacherRelation(teacherId,team.getId());
        return Result.success(integer>0);
    }

    @Override
    @Transactional
    public Result deleteTeam(Long teamId) {
        teamMapper.deleteTeam(teamId);
        Integer integer =  teamMapper.deleteTeamAndTeacherRelation(teamId);
        return Result.success(integer>0);
    }

    @Override
    public Result updateTeam(Team team) {
        Integer integer = teamMapper.updateTeam(team);
        return Result.success(integer>0);
    }

    @Override
    public boolean updateTeamCounts(Long teamId){
        Integer integer = teamMapper.updateTeamCounts(teamId);
        return integer>0;
    }

    @Override
    public List<Team> findAllTeam() {
        return teamMapper.findAllTeam();
    }

    @Override
    public Result findTeamById(Long id) {
        Team teamById = teamMapper.findTeamById(id);
        return Result.success(teamById);
    }

}
