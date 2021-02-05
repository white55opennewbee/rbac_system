package com.pc.rbac_system.model;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Daily extends BaseModel implements Serializable {
    private String dailyTitle;
    private String dailyBody;
    private Date dailyTime;
    private String dailyStatus;
}
