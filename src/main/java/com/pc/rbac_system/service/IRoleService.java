package com.pc.rbac_system.service;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Role;
import com.pc.rbac_system.vo.RoleSearchParam;

import java.util.List;

public interface IRoleService {
    Result findAllRoles();
    Result findRoleById(Long id);
    Result findRoleByName(String name);
    Result deleteRoleById(Long id);
    Result addRole(Role role);
    Result updateRole(Role role);

    List<Role> findRolesByUserId(Long id);

    Result findRolesBySearch(RoleSearchParam param);

    Integer updateRoleAndTeamLeaderRelation(Long oldTeamLeaderUserId, Long newTeamLeaderUserId);

    Integer updateRoleAndTeamLeaderRelation(Long newTeamLeaderUserId);
}
