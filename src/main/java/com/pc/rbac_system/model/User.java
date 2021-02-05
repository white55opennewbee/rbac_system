package com.pc.rbac_system.model;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class User extends BaseModel {
    private String username;
    private String password;
    private String icon;
    private String email;
    private String nickName;
    private String note;
    private Date loginTime;
}
