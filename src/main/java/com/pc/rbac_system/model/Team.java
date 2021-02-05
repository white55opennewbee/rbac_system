package com.pc.rbac_system.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Team extends BaseModel {
    private String  teamName;
    private String level;
    private String type;
    private Long groupLeaderId;
    private Integer memberCounts;
    private String classroom;
    private Long coachId;
}
