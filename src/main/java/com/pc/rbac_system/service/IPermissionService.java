package com.pc.rbac_system.service;

import com.pc.rbac_system.model.Permission;
import com.pc.rbac_system.model.Role;

import java.util.List;

public interface IPermissionService {
    List<Permission> findPermissionsByUserId(Long id);
    List<Permission> findPermissionsByRoleId(Long id);

    List<Permission> findAllPermission();

    Integer  updateRolePermissions(Role role);
}
