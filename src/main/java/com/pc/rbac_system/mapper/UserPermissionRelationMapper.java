package com.pc.rbac_system.mapper;

import com.pc.rbac_system.vo.UserAndAuthentication;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserPermissionRelationMapper {
    Integer setPermission(UserAndAuthentication userAndAuthentication);
    Integer deleteUserPermission(UserAndAuthentication userAndAuthentication);
}
