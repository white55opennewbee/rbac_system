package com.pc.rbac_system.model;

import lombok.*;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class LoginInfo{
    private Date loginTime;
    private String username;
    private String nickName;
    private String ip;
    private String addr;
    private String id;
}
