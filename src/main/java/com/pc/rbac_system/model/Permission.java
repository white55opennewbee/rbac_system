package com.pc.rbac_system.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Permission extends BaseModel implements GrantedAuthority {
    private Long pid;
    private String name;
    //权限
    private String value;
    private String icon;
    private Integer type;
    private String uri;
    private Integer sort;


    @Override
    public String getAuthority() {
        return this.value;
    }

}
