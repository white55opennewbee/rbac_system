package com.pc.rbac_system.service;

import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Daily;
import com.pc.rbac_system.vo.DailySearchParam;
import io.swagger.models.auth.In;

import java.util.Date;
import java.util.List;


public interface IDailyService {
    PageInfo findAllDailyBySearch(DailySearchParam param);

    Result updateDaily(Daily daily);

    Result addDaily(Daily daily,String username);

    Result deleteDaily(Long dailyId);

    Result updateDailyStatus(String statusName,Long dailyId);

    Integer updateDailyStatus(String statusName, Long dailyId, Date date);

    Result findDailyByDailyId(Long id);

    List<Daily> findDailyByTeacherId(Long teacherId,DailySearchParam dailySearchParam);
}
