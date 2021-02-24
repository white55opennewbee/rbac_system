package com.pc.rbac_system.controller;

import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.common.ExportWord;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.dto.TodayDailyPutState;
import com.pc.rbac_system.model.Daily;
import com.pc.rbac_system.service.IDailyService;
import com.pc.rbac_system.vo.DailySearchParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/setDailyStatusToRead/{id}")
    @PreAuthorize("hasAuthority('wx:daily:update:read')")
    public Result setDailyStatusToRead(@PathVariable("id")Long id){
        Result result = dailyService.updateDailyStatus("已查看", id);
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

    @PostMapping("/findDailyByTeacherId/{teacherId}")
    public Result findDailyByTeacherId(@RequestBody DailySearchParam dailySearchParam,@PathVariable Long teacherId){
        PageInfo dailies = dailyService.findDailyByTeacherId(teacherId,dailySearchParam);
        return Result.success(dailies);
    }

    @GetMapping("/findTodayDailyPutStatus/{teacherId}/{currentPage}/{maxSize}")
    public Result findTodayDailyPutStatusByTeacherId(@PathVariable Long teacherId,@PathVariable Integer currentPage,@PathVariable Integer maxSize){
        PageInfo dailyPutState = dailyService.findTodayDailyPutStatus(teacherId,currentPage,maxSize);
        return Result.success(dailyPutState);
    }


    @GetMapping("/CreateDailyWord/{dailyId}")
    public void CreateDailyWord(HttpServletResponse response, @PathVariable Long dailyId){
        dailyService.CreateDailyWord(dailyId,response);
    }


}
