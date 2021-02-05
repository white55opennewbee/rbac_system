package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.FileUpAndDownload;
import com.pc.rbac_system.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/common")
@RestController
public class CommonController {
    @PostMapping("/uploadImg")
    public Result uploadImg(@RequestParam("myFile")MultipartFile file){
        String contextPath = "D:\\rbac_static_web\\myhtml\\img";
        Result result = FileUpAndDownload.ImguploadFile(file, contextPath);
        return result;
    }
}
