package com.pc.rbac_system.mapper;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Team;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeamMapper {
    List<Team> findAllTeam();

    List<Team> findAllTeamByTeacherId(Long teacherId);

    Integer addTeam(Team team);

    Integer addTeamAndTeacherRelation(@Param("teacherId") Long teacherId,@Param("teamId") Long teamId);

    Result deleteTeam(Long teamId);

    Integer deleteTeamAndTeacherRelation(Long teamId);

    Integer updateTeam(Team team);

    Integer updateTeamCounts(Long teamId);

    Team findTeamById(Long id);


}
