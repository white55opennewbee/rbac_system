package com.pc.rbac_system.mapper;


import com.pc.rbac_system.model.Role;
import com.pc.rbac_system.vo.RoleSearchParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    List<Role> findAllRoles();
    Role findRoleById(Long id);
    Role findRoleByName(String name);
    Integer deleteRoleById(Long id);
    Integer addRole(Role role);

    List<Role> findRolesByUserId(Long userId);

    Integer updateRole(Role role);

    List<Role> findRolesBySearch(RoleSearchParam param);
}
