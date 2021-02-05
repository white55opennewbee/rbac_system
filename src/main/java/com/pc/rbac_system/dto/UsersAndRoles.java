package com.pc.rbac_system.dto;

import com.pc.rbac_system.model.Role;
import com.pc.rbac_system.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UsersAndRoles {
    private User user;
    private List<Role> roles;
}
