package com.pc.rbac_system.service;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.AllocateRecord;

public interface IAllocateRecordService {

    AllocateRecord createAllocateRecord(AllocateRecord allocateRecord);

    Result<AllocateRecord> findAllcateRecordsByStudentName(String studentName,Integer currentPage,Integer maxSize);

}
