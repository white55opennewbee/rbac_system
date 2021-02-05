package com.pc.rbac_system.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseModel {
    String name;
    String description;
    Long adminCount;

    List<Permission> permissions;
}
