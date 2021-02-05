package com.pc.rbac_system.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class AllocateRecord extends BaseModel {
    private Long studentId;
    private String oldTeam;
    private String newTeam;
}
