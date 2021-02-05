package com.pc.rbac_system.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class Student extends BaseModel {
    private Long userId;
    private String studentName;
    private String studentClassNum;
    private Long teamId;
    private String location;
}
