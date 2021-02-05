package com.pc.rbac_system.vo;

import com.pc.rbac_system.model.Daily;
import lombok.Data;

@Data
public class DailyUpdateParam {
    private Long stuId;
    private Daily daily;
}
