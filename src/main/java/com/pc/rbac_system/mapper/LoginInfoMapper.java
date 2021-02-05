package com.pc.rbac_system.mapper;

import com.pc.rbac_system.model.LoginInfo;

import java.util.List;

public interface LoginInfoMapper {
    List<LoginInfo> findAllLoginInfos();
}
