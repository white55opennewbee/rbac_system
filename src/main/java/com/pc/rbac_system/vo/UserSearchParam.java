package com.pc.rbac_system.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserSearchParam {
    private String username;
    private String email;
    private Date createTimeBegin;
    private Date createTimeEnd;
    private Page page;
}
