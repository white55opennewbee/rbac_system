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

    @GetMapping("/findAllcateRecordsByStudentName/{currentPage}/{maxSize}/{studentName}")
    public Result findAllcateRecordsByStudentName(@PathVariable Integer currentPage, @PathVariable Integer maxSize,@PathVariable String studentName){
        return  allocateRecordService.findAllcateRecordsByStudentName(studentName, currentPage, maxSize);
    }
}
