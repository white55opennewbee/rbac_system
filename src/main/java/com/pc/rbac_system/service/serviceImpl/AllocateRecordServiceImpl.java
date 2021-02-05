package com.pc.rbac_system.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.mapper.AllocateRecordMapper;
import com.pc.rbac_system.model.AllocateRecord;
import com.pc.rbac_system.service.IAllocateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AllocateRecordServiceImpl implements IAllocateRecordService {
    @Autowired
    AllocateRecordMapper allocateRecordMapper;
    @Override
    public AllocateRecord createAllocateRecord(AllocateRecord allocateRecord){
        allocateRecordMapper.createAllocateRecord(allocateRecord);
        return allocateRecord;
    }

    @Override
    public Result findAllcateRecordsByStudentName(String studentName, Integer currentPage, Integer maxSize) {
        PageHelper.startPage(currentPage,maxSize);
        List<AllocateRecord> allocateRecordList = allocateRecordMapper.findAllcateRecordsByStudentName(studentName);
        PageInfo info = new PageInfo(allocateRecordList);
        return Result.success(info);
    }
}
