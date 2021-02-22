package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.service.IAllocateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/allocateRecord")
public class AllocateRecordController {
    @Autowired
    IAllocateRecordService allocateRecordService;

    @GetMapping(path = {"/findAllcateRecordsByStudentName/{currentPage}/{maxSize}/{studentName}","/findAllcateRecordsByStudentName/{currentPage}/{maxSize}/"})
    public Result findAllcateRecordsByStudentName(@PathVariable Integer currentPage, @PathVariable Integer maxSize,@PathVariable(required = false) String studentName){
        String s = studentName;
        if (null == s){
            s = "";
        }
        return  allocateRecordService.findAllcateRecordsByStudentName(s, currentPage, maxSize);
    }
}
