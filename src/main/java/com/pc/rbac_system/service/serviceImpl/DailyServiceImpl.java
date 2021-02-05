package com.pc.rbac_system.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.mapper.DailyMapper;
import com.pc.rbac_system.model.Daily;
import com.pc.rbac_system.service.IDailyService;
import com.pc.rbac_system.vo.DailySearchParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DailyServiceImpl implements IDailyService {
    @Autowired
    DailyMapper dailyMapper;
    @Override
    public PageInfo findAllDailyBySearch(DailySearchParam param) {
        PageHelper.startPage(param.getPage().getCurrentPage(),param.getPage().getMaxSize());
        List<Daily> allDailyByStudentId = dailyMapper.findAllDailyBySearch(param);
        PageInfo info = new PageInfo(allDailyByStudentId);
        return info;
    }

    @Override
    public Result updateDaily(Daily daily) {
        Integer effect = dailyMapper.updateDaily(daily);
        if (effect>0){
            return Result.success(true);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    @Transactional
    public Result addDaily(Daily daily, String username){
        daily.setCreateTime(new Date(System.currentTimeMillis()));
        dailyMapper.addDaily(daily);
        Integer integer = dailyMapper.insertWxUserDailyRelation(username, daily.getId());
        return Result.success(integer>0);
    }

    @Override
    public Result deleteDaily(Long dailyId) {
        Integer effect = dailyMapper.deleteDailyById(dailyId);
        return Result.success(effect>0);
    }

    @Override
    public Result updateDailyStatus(String statusName, Long dailyId) {
        Integer integer;
        if (statusName.equalsIgnoreCase("已提交")){
             integer = updateDailyStatus(statusName,dailyId,new Date(System.currentTimeMillis()));
        }else {
             integer = dailyMapper.updateDailyStatus(dailyId,statusName);
        }

        return Result.success(integer>0);
    }

    @Override
    public Integer updateDailyStatus(String statusName, Long dailyId, Date date) {
        return dailyMapper.updateDailyStatusAndPutTime(statusName,dailyId,date);
    }

    @Override
    public Result findDailyByDailyId(Long id) {
        Daily daily = dailyMapper.findDailyByDailyId(id);
        return Result.success(daily);
    }


}
