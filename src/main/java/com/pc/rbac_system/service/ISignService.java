package com.pc.rbac_system.service;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Sign;

import java.util.List;

public interface ISignService {
    Result findTodaySignByTeamId(Long teamId, Integer currentPage, Integer maxSize);

    Integer putAll(List<Sign> signs);

    boolean SetTodaySignType(Integer type, Long studentId, Long teamId);
}
