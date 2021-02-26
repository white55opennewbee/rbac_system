package com.pc.rbac_system.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseModel implements GrantedAuthority {
    String name;
    String description;
    Long adminCount;

    List<Permission> permissions;

    @Override
    public String getAuthority() {
        return "ROLE_"+this.getName();
    }
}
