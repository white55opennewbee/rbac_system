package com.pc.rbac_system.vo;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RoleSearchParam {
    private String name;
    private Long adminCountMin;
    private Long adminCountMax;
    private Date startTime;
    private Date endTime;
    private Page page;
}
