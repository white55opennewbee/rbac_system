package com.pc.rbac_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.text.SimpleDateFormat;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsLoginInfo {
        //date 格式要求 yyyy-MM-dd HH:mm:ss 或者 时间戳
        private Date loginTime;
        private String username;
        private String nickName;
        private String ip;
        private String addr;
        private String loginInfoId;
        private Long id;

//        public String getLoginTime() {
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
//                format.format(loginTime)
//                return loginTime;
//        }
}
