package com.pc.rbac_system.service.serviceImpl;

import com.pc.rbac_system.model.Permission;
import com.pc.rbac_system.model.Role;
import com.pc.rbac_system.model.User;
import com.pc.rbac_system.common.UserAndPermission;
import com.pc.rbac_system.service.IPermissionService;
import com.pc.rbac_system.service.IRoleService;
import com.pc.rbac_system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service("userDetailsService")
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserService userService;
    @Autowired
    IPermissionService permissionService;
    @Autowired
    IRoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User userByUsername = userService.findUserByUsername(s);
        List<Permission> permissionsByUserId = permissionService.findPermissionsByUserId(userByUsername.getId());
        List<Role> roles = roleService.findRolesByUserId(userByUsername.getId());
        UserAndPermission userAndPermission = new UserAndPermission();
        userAndPermission.setRoles(new HashSet<>(roles));
        userAndPermission.setUser(userByUsername);
        userAndPermission.setAuthorities(new HashSet<>(permissionsByUserId));
        return userAndPermission;
    }
}
