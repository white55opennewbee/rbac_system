package com.pc.rbac_system.model;

import lombok.*;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Sign extends BaseModel {
    private Long studentId;
    private Integer signIn;
    private String studentName;
    private Date lateTime;
}
