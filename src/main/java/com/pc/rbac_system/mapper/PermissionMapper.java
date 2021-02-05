package com.pc.rbac_system.mapper;

import com.pc.rbac_system.model.Permission;
import com.pc.rbac_system.model.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {
    List<Permission> findPermissionsByUserId(Long userId);

    List<Permission> findPermissionsByRoleId(Long roleId);

    List<Permission> findAllPermissions();

    Integer deletePermissionByRole(Role role);

    Integer insertPermissionsByRole(Role role);
}
