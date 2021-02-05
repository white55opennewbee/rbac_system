package com.pc.rbac_system.vo;

import com.pc.rbac_system.model.User;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class UserAndRoleParam {
    private User user;
    private List<Long> roleId;
}
