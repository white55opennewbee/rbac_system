package com.pc.rbac_system.model;

import lombok.*;

import java.util.Date;

@Data
public class BaseModel {
   private Long id;
   private Date createTime;
   private Byte status;
}
