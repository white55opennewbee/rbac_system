package com.pc.rbac_system.dto;

import com.pc.rbac_system.model.Daily;
import com.pc.rbac_system.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodayDailyPutState {
    Daily daily;
    Student student;
}
