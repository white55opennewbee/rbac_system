package com.pc.rbac_system.mapper;

import com.pc.rbac_system.model.Sign;
import com.pc.rbac_system.model.SignStatus;
import com.pc.rbac_system.vo.SignSearchParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SignMapper {
    Integer putAll(List<Sign> signs);

    List<SignStatus> findAllSignStatus();

    List<Sign> findSignsRecordByPage(@Param("signSearchParam") SignSearchParam signSearchParam,@Param("teacherId") Long teacherId);

    Integer changeStudentSignType(@Param("signTypeId") Long signTypeId,@Param("signId") Long signId);
}
