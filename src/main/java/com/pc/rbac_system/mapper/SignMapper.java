package com.pc.rbac_system.mapper;

import com.pc.rbac_system.model.Sign;
import com.pc.rbac_system.model.SignStatus;

import java.util.List;

public interface SignMapper {
    Integer putAll(List<Sign> signs);

    List<SignStatus> findAllSignStatus();
}
