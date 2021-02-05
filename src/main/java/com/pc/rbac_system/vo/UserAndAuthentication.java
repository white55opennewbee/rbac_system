package com.pc.rbac_system.vo;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserAndAuthentication {
    private Integer userId;
    private List<Integer> permissionIds;
    private List<Integer> type;
}
