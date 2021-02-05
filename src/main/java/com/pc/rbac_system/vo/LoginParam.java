package com.pc.rbac_system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginParam {
    private String username;
    private String password;
    private String ip;
    private String addr;
}
