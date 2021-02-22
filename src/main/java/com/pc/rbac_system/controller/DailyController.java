package com.pc.rbac_system.controller;

import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.Daily;
import com.pc.rbac_system.service.IDailyService;
import com.pc.rbac_system.vo.DailySearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/daily")
public class DailyController {
    @Autowired
    IDailyService dailyService;

    @PostMapping("/findAllDailyByStudentName")
    @PreAuthorize("hasAnyAuthority('wx:daily:read:self','wx:daily:read:class')")
    public Result findAllDailyByStudentName(@RequestBody DailySearchParam param){
       PageInfo pageInfo =  dailyService.findAllDailyBySearch(param);
       return Result.success(pageInfo);
    }

    @PostMapping(value = {"/updateDaily","/updateDaily/{type}"})
    @PreAuthorize("hasAuthority('wx:daily:update')")
    public Result updateDaily(@RequestBody Daily daily,@PathVariable(value = "type",required = false) String type){
        Result result = dailyService.updateDaily(daily);
        if (null != type&&type.equalsIgnoreCase("put")){
            result = dailyService.updateDailyStatus("已提交", daily.getId());
        }
        return result;
    }
    @PostMapping("/setDailyStatusToPut/{id}")
    @PreAuthorize("hasAuthority('wx:daily:update')")
    public Result setDailyStatusToPut(@PathVariable("id")Long id){
        Result result = dailyService.updateDailyStatus("已提交", id);
        return Result.success(result);
    }


    @PostMapping(value = {"/addDaily/{username}","/addDaily/{username}/{type}"})
    @PreAuthorize("hasAuthority('wx:daily:create')")
    public Result addDaily(@RequestBody Daily daily,@PathVariable("username") String username,@PathVariable(value = "type",required = false)String type){
        Result result = dailyService.addDaily(daily, username);
        if (null!=type&&type.equalsIgnoreCase("put")){
            result = dailyService.updateDailyStatus("已提交", daily.getId());
        }
        return result;
    }

    @DeleteMapping("/deleteDaily/{dailyId}")
    @PreAuthorize("hasAuthority('wx:daily:delete')")
    public Result deleteDaily(@PathVariable("dailyId") Long dailyId){
        Result result = dailyService.deleteDaily(dailyId);
        return result;
    }

    @PostMapping("/updateDailyStatus/{statusName}/{dailyId}")
    @PreAuthorize("hasAuthority('wx:daily:update:read')")
    public Result updateDailyStatus(@PathVariable("statusName") String statusName,@PathVariable("dailyId") Long dailyId){
        Result result = dailyService.updateDailyStatus(statusName, dailyId);
        return result;
    }

    @GetMapping("/findDailyByDailyId/{id}")
    public Result findDailyByDailyId(@PathVariable("id")Long id){
        Result result = dailyService.findDailyByDailyId(id);
        return result;
    }

    @GetMapping("/findDailyByTeacherId/{teacherId}")
    public Result findDailyByTeacherId(@RequestBody DailySearchParam dailySearchParam,@PathVariable Long teacherId){
        List<Daily> dailies = dailyService.findDailyByTeacherId(teacherId,dailySearchParam);
        return Result.success(dailies);
    }

}
