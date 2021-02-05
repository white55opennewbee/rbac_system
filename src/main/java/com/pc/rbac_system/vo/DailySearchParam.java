package com.pc.rbac_system.vo;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class DailySearchParam {
    private List<String> dailyStatus;
    private Date startTime;
    private Date endTime;
    private String studentName;
    private String dailyTitle;
    private Page page;
}
