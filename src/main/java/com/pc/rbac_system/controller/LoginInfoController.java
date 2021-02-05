package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.EsLoginInfo;
import com.pc.rbac_system.service.IESLoginInfoService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/loginInfo")
public class LoginInfoController {
    @Autowired
    IESLoginInfoService esLoginInfoService;

    @PreAuthorize("hasAuthority('wx:role:read')")
    @GetMapping(value ={"/findLoginInfoBySearch/{currentPage}/{maxSize}/{keyWord}","/findLoginInfoBySearch/{currentPage}/{maxSize}"})
    public Result findLoginInfoBySearch(@PathVariable(value = "keyWord",required = false) String keyWord, @PathVariable("currentPage") Integer currentPage, @PathVariable("maxSize") Integer maxSize){
        try {
            List<EsLoginInfo> search = esLoginInfoService.search(keyWord, currentPage, maxSize);
            return Result.success(search);
        } catch (IOException e) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
