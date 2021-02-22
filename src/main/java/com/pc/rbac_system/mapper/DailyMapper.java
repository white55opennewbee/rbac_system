package com.pc.rbac_system.mapper;

import com.pc.rbac_system.model.Daily;
import com.pc.rbac_system.vo.DailySearchParam;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DailyMapper {
    List<Daily> findAllDailyBySearch(DailySearchParam param);

    Integer updateDaily(Daily daily);

    void addDaily(Daily daily);

    Integer deleteDailyById(Long id);

    Integer updateDailyStatus(@Param("id") Long id,@Param("statusName") String dailyStatus);

    Integer insertWxUserDailyRelation(@Param("username") String  username,@Param("dailyId") Long dailyId);

    Daily findDailyByDailyId(Long id);

    Integer updateDailyStatusAndPutTime(@Param("statusName")String dailyStatus,@Param("id") Long id,@Param("date") Date date);

    List<Daily> findDailyByTeacherId(@Param("teacherId") Long teacherId,@Param("dailySearchParam") DailySearchParam dailySearchParam);
}
