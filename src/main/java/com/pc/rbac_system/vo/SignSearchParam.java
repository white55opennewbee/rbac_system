package com.pc.rbac_system.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SignSearchParam {
    private String studentName;
    private Date startTime;
    private Date endTime;
}
