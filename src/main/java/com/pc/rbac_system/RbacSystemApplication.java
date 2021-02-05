package com.pc.rbac_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@MapperScan("com.pc.rbac_system.mapper")
public class RbacSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(RbacSystemApplication.class, args);
    }
}
