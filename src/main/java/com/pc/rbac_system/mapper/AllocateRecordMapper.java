package com.pc.rbac_system.mapper;

import com.pc.rbac_system.model.AllocateRecord;

import java.util.List;

public interface AllocateRecordMapper {
    Integer createAllocateRecord(AllocateRecord allocateRecord);

    List<AllocateRecord> findAllcateRecordsByStudentName(String studentName);
}
