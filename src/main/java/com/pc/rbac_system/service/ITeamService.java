package com.pc.rbac_system.service;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Team;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public interface ITeamService {
    Result findAllTeam(Integer currentPage, Integer maxSize,Long teacherId);

    Result addTeam(Team team, Long teacherId);

    Result deleteTeam(Long teamId);

    Result updateTeam(Team team);

    boolean updateTeamCounts(Long teamId);

    List<Team> findAllTeam();

    Result findTeamById(Long id);

    Result setTeamLeader(Long stuId, Long teamId);
}
